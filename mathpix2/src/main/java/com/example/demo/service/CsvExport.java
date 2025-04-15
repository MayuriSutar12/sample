package com.example.demo.service;

import com.example.demo.model.OptionsData;
import com.example.demo.projectionInterface.QuestionInfoProjection;
import com.example.demo.projectionInterface.QuestionUrlInterface;
import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.repository.QuestionInfoRepository;
import com.example.demo.repository.QuestionS3UrlsRepository;
import com.example.demo.resp_dto.QuestionJsonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class CsvExport {
    @Value("${api.solution-url}")
    private String apiUrl;
    final String imgBaseUrl = "https://jeeni-question-images.s3.ap-south-1.amazonaws.com/";

    @Autowired
    QuestionS3UrlsRepository questionS3UrlsRepository;
    @Autowired
    private QuestionInfoRepository questionInfoRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OptionsDataRepository optionsDataRepository;

    public void writeCsvToResponse(HttpServletResponse response, int offset) throws IOException {
        // Fetch 100 records starting from the given offset
        List<QuestionUrlInterface> questions = questionS3UrlsRepository.getQuestionDetailsPaginated(offset);

        // Set response headers for CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"questions_" + offset + ".csv\"");

        try (PrintWriter writer = response.getWriter()) {
            // Write CSV header
            writer.println("Question ID,Question Type,Question Subtype,Question URL");

            // Write each row to the CSV
            for (QuestionUrlInterface question : questions) {
                String questionUrl = (question.getQuestionUrl() != null)
                        ? imgBaseUrl.concat(question.getQuestionUrl())
                        : "N/A";

                writer.printf("%d,%d,%d,%s%n",
                        question.getQuestionId(),
                        question.getQuestionType(),
                        question.getQuestionSubType(),
                        questionUrl);


            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }


    public void processCsvFile(MultipartFile file, HttpServletResponse response) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
// Add Jackson ObjectMapper to convert DTO to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            // Setup response for CSV download
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=processed_questions.csv");

            try (PrintWriter writer = response.getWriter();
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                         "Question ID", "Question Latex", "Options Latex", "Solution Latex", "Correct Answer From Database",
                         "Answer Key from API", "Matched?", "Complexity", "Topics", "QuestionDataJson", "options"))) {

                for (CSVRecord record : records) {
                    try {
                        Long questionId = Long.parseLong(record.get("Question ID"));

                        log.info("QuestionId" + questionId);
//                            int questionType = Integer.parseInt(record.get("Question Type"));
//                            int questionSubType = Integer.parseInt(record.get("Question Subtype"));
//                            String questionUrl = record.get("Question URL");

                        // Fetch Question details from Database
                        QuestionInfoProjection questionInfo = questionInfoRepository.getQuestionInfoById(questionId);
                        if (questionInfo == null) {
                            log.warn("No question found for ID: {}", questionId);
                            continue;
                        }
                        String questionUrl = imgBaseUrl.concat(questionInfo.getGenericUrl());
                        // Extract details from database response
                        //  String questionUrl = questionInfo.getGenericUrl();
                        int questionType = questionInfo.getQuestionType();
                        int questionSubType = questionInfo.getQuestionSubType();
                        QuestionJsonDTO questionJsonDTO = callGetSolTextApi(questionId, questionUrl, questionType, questionSubType);
                        if (questionJsonDTO == null) {
                            log.warn("No solution data found for questionId: {}", questionId);
                            continue;
                        }

                        List<OptionsData> optionsDataList = optionsDataRepository.fetchOptionsByQuestionId(questionId);
                        log.info("Options data for questionId: {}: {}", questionId, optionsDataList.size());

                        String correctAnswer = "";
                        String optionsFormatted = "";
                        boolean matched = false;

                        if (questionType == 3) {
                            correctAnswer = processNumericOptions(optionsDataList);
                            matched = correctAnswer.equalsIgnoreCase(questionJsonDTO.getAnswerKey());
                        } else if (questionType == 6) {
                            correctAnswer = processNumericAnswer(optionsDataList);
                            matched = compareNumericAnswers(correctAnswer, questionJsonDTO.getAnswerKey());
                        } else {
                            correctAnswer = processMultipleChoiceOptions(optionsDataList, questionType);
                            matched = compareMultipleAnswers(correctAnswer, questionJsonDTO.getAnswerKey());
                        }

                        // Convert DTO to JSON string
                        String questionDataJson = objectMapper.writeValueAsString(questionJsonDTO);
                        csvPrinter.printRecord(
                                questionId,
                                (questionJsonDTO.getPassageLatex() != null ? questionJsonDTO.getPassageLatex() : "") +
                                        (questionJsonDTO.getQuestionLatex() != null ? questionJsonDTO.getQuestionLatex() : ""),
                                questionJsonDTO.getOptionsLatex() != null ? questionJsonDTO.getOptionsLatex() : "",
                                questionJsonDTO.getSolutionLatex() != null ? questionJsonDTO.getSolutionLatex() : "",
                                correctAnswer != null ? correctAnswer : "",
                                questionJsonDTO.getAnswerKey() != null ? questionJsonDTO.getAnswerKey() : "",
                                matched ? "Yes" : "No",
                                questionJsonDTO.getComplexity(),
                                questionJsonDTO.getTopics() != null ? questionJsonDTO.getTopics() : "",
                                questionDataJson,
                                questionJsonDTO.getOptions()
                        );


                    } catch (Exception e) {
                        log.error("Error processing record: {}", record, e);
                    }
                }
                csvPrinter.flush();
            }
        } catch (IOException e) {
            log.error("Error while processing CSV", e);
            throw new IOException("Error while processing CSV", e);
        }
    }

    private String processNumericOptions(List<OptionsData> optionsDataList) {
        String[] optionLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder optionsFormatted = new StringBuilder();
        String correctAnswer = "";

        for (int i = 0; i < Math.min(optionsDataList.size(), 10); i++) {
            String option = optionLabels[i] + "-" + optionsDataList.get(i).getText_data();
            optionsFormatted.append(option).append(", ");
            if (Boolean.TRUE.equals(optionsDataList.get(i).getIs_answer())) {
                correctAnswer = optionLabels[i];
            }
        }

        return correctAnswer;
    }

    private String processNumericAnswer(List<OptionsData> optionsDataList) {
        for (OptionsData optionData : optionsDataList) {
            if (optionData.getAnswer() != null) {
                return optionData.getAnswer().trim();
            }
        }
        return "N/A";
    }

    //        private boolean compareNumericAnswers(String correctAnswer, String answerKeyFromApi) {

    private boolean compareNumericAnswers(String correctAnswer, String answerKeyFromApi) {
        if (correctAnswer.equals("N/A") || answerKeyFromApi == null || answerKeyFromApi.isEmpty()) {
            return false;
        }
        try {
            // Convert to lowercase and trim whitespace
            return correctAnswer.trim().equalsIgnoreCase(answerKeyFromApi.trim());
        } catch (NumberFormatException e) {
            log.error("Error parsing numeric answers: correctAnswer={}, answerKeyFromApi={}", correctAnswer, answerKeyFromApi, e);
            return false;
        }
    }

    private String processMultipleChoiceOptions(List<OptionsData> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        StringBuilder optionsFormatted = new StringBuilder();
        Set<String> correctAnswersSet = new HashSet<>();

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            String option = optionLabels[i] + "-" + optionsDataList.get(i).getText_data();
            optionsFormatted.append(option).append(", ");

            if (Boolean.TRUE.equals(optionsDataList.get(i).getIs_answer())) {
                if (questionType == 7) {
                    // Concatenate correct answer only if questionType is 7
                    correctAnswersSet.add(optionLabels[i] + "-" + optionsDataList.get(i).getAnswer());
                } else {
                    // Otherwise, just store the option label
                    correctAnswersSet.add(optionLabels[i]);
                }
            }
        }

        // Remove last comma if needed
        if (optionsFormatted.length() > 0) {
            optionsFormatted.setLength(optionsFormatted.length() - 2);
        }

        return String.join(",", correctAnswersSet);
    }

//        private boolean compareMultipleAnswers(String correctAnswer, String answerKeyFromApi) {
//            if (answerKeyFromApi == null) {
//                return false;
//            }
//            Set<String> apiAnswersSet = new HashSet<>(Arrays.asList(answerKeyFromApi.split(",")));
//            Set<String> correctAnswersSet = new HashSet<>(Arrays.asList(correctAnswer.split(",")));
//            return correctAnswersSet.equals(apiAnswersSet);
//        }

    private boolean compareMultipleAnswers(String correctAnswer, String answerKeyFromApi) {
        if (answerKeyFromApi == null) {
            return false;
        }

        // Convert both sets to lowercase before comparison
        Set<String> apiAnswersSet = new HashSet<>(
                Arrays.asList(answerKeyFromApi.toLowerCase().split(","))
        );

        Set<String> correctAnswersSet = new HashSet<>(
                Arrays.asList(correctAnswer.toLowerCase().split(","))
        );

        return correctAnswersSet.equals(apiAnswersSet);
    }


    public QuestionJsonDTO callGetSolTextApi(Long questionId, String questionUrl, int questionType, int questionSubType) {
      //  String apiUrl = "http://13.202.253.67:5000/getSolution";

        try {

            // Construct the full URL with all parameters
            String fullUrl = String.format("%s?questionId=%d&questionUrl=%s&questionType=%d&questionSubtype=%d",
                    apiUrl, questionId, questionUrl, questionType, questionSubType);

            // Send GET request
            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl, HttpMethod.GET, null, String.class);

            // Log status code and response body
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                ModelMapper modelMapper = new ModelMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(response.getBody().toString(), Map.class);

                QuestionJsonDTO questionJsonDTOsss = modelMapper.map(jsonMap, QuestionJsonDTO.class);
                System.out.println("Converted DTO: " + questionJsonDTOsss);


                final QuestionJsonDTO questionJsonDTO = objectMapper.readValue(response.getBody(), QuestionJsonDTO.class);
                log.info("Object: {}", questionJsonDTO);
                return questionJsonDTOsss;
            } else {
                System.out.println("Failed to retrieve solution. Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

