package com.example.demo.service;

import com.example.demo.exception.CustomApiException;
import com.example.demo.model.OptionsDataTemp;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.QuestionInfoAI;
import com.example.demo.model.SolutionDataTemp;
import com.example.demo.repository.*;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.resp_dto.QuestionJsonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ProcessTempListBatch {


    final String imgBaseUrl = "https://jeeni-question-images.s3.ap-south-1.amazonaws.com/";
    @Autowired
    private OptionsDataRepository optionsDataRepository;
    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private OptionsDataTempRepository optionsDataTempRepository;
    @Autowired
    private SolutionDataTempRepository solutionDataTempRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QuestionInfoAiRepository questionInfoAiRepository;

    public ApiResponse processTempList() {
        int batchSize = 1500;
        List<QuestionDataTemp> questionsBatch = questionDataTempRepository.fetchUnprocessedQuestions(batchSize);

        if (questionsBatch.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "No more questions to process.", null);
        }
          //  Prevent processing if batch size >= 1500
        if (questionsBatch.size() >= batchSize) {
            return new ApiResponse(HttpStatus.TOO_MANY_REQUESTS, "Resource exhausted: Too many questions to process at once.", null);
        }
        StringBuilder errorMessages = new StringBuilder();
        if (questionsBatch.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "No questions found to process.", null);
        }
        for (QuestionDataTemp question : questionsBatch) {
            Long questionId = question.getQuestionId();
            boolean matched = false;
            String correctAnswer = null;
            QuestionJsonDTO questionJsonDTO = null;

            try {
                questionJsonDTO = callGetSolTextApi(
                        questionId,
                        question.getQuestionImageUrl(),
                        question.getQuestionType(),
                        question.getQuestionSubType()
                );

                if (questionJsonDTO == null) {
                    log.warn("No solution data found for questionId: {}", questionId);
                    continue;
                }

                List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
                log.info("Options data for questionId: {}: {}", questionId, optionsDataTempList.size());

                if (question.getQuestionType() == 3) {
                    correctAnswer = processNumericOptions(optionsDataTempList);
                    matched = correctAnswer.equalsIgnoreCase(questionJsonDTO.getAnswerKey());
                } else if (question.getQuestionType() == 6) {
                    correctAnswer = processNumericAnswer(optionsDataTempList);
                    matched = compareNumericAnswers(correctAnswer, questionJsonDTO.getAnswerKey());
                } else {
                    correctAnswer = processMultipleChoiceOptions(optionsDataTempList, question.getQuestionType());
                    matched = compareMultipleAnswers(correctAnswer, questionJsonDTO.getAnswerKey());
                }

                Optional<QuestionDataTemp> existingQuestionOpt = questionDataTempRepository.findById(questionId);
                if (existingQuestionOpt.isPresent()) {
                    QuestionDataTemp existingQuestion = existingQuestionOpt.get();
                    existingQuestion.setQuestionLatex(questionJsonDTO.getPassageLatex().concat(questionJsonDTO.getQuestionLatex()));
                    existingQuestion.setComplexity(questionJsonDTO.getComplexity());
                    existingQuestion.setIsAnsMatch(matched);
                    existingQuestion.setGenAiConversionCompleted(true);
                    existingQuestion.setUpdatedOn(LocalDateTime.now());

                    questionDataTempRepository.save(existingQuestion);
                } else {
                    log.warn("No existing record found for questionId: {}", questionId);
                }

                List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
                if (!existingOptions.isEmpty()) {
                    processAndSaveOptions(questionJsonDTO.getAnswerKey(), question.getQuestionType(), questionJsonDTO, question.getQuestionSubType());
                }

                SolutionDataTemp existingSolution = solutionDataTempRepository.fetchSolutionForQuestionId(questionId);
                if (existingSolution != null) {
                    existingSolution.setSolutionLatex("Ans is:" + questionJsonDTO.getAnswerKey() + " " + questionJsonDTO.getSolutionLatex());
                    solutionDataTempRepository.save(existingSolution);
                }

                QuestionInfoAI questionInfoAI = questionInfoAiRepository.getForQuestion(questionId);
                if (questionInfoAI != null) {
                    // Update existing entry
                    String topicsString = (questionJsonDTO.getTopics() != null) ? String.join(",", questionJsonDTO.getTopics()) : "";
                    questionInfoAI.setStatusAi(matched);
                    questionInfoAI.setGenAiTopics(topicsString);
                    questionInfoAI.setComplexity(questionJsonDTO.getComplexity());
                    questionInfoAI.setAiAnswer(questionJsonDTO.getAnswerKey());

                    log.info("Updating existing QuestionInfoAI entry for questionId: {}", questionId);
                    questionInfoAiRepository.save(questionInfoAI);
                } else {
                    // Create new entry
                    questionInfoAI = new QuestionInfoAI();
                    questionInfoAI.setQuestionId(questionId);
                    questionInfoAI.setStatusAi(matched);
                    questionInfoAI.setGenAiTopics((questionJsonDTO.getTopics() != null) ? String.join(",", questionJsonDTO.getTopics()) : "");
                    questionInfoAI.setComplexity(questionJsonDTO.getComplexity());
                    questionInfoAI.setAiAnswer(questionJsonDTO.getAnswerKey());

                    log.info("Saving new QuestionInfoAI entry for questionId: {}", questionId);
                    questionInfoAiRepository.save(questionInfoAI);
                }


            } catch (Exception e) {
                String errorMessage = "Error processing questionId " + questionId + ": " + e.getMessage();
                log.error(errorMessage, e);
                errorMessages.append(errorMessage).append("\n");
            } finally {
                // Ensure genAiConversionCompleted is set to true even if an error occurs
                Optional<QuestionDataTemp> existingQuestionOpt = questionDataTempRepository.findById(questionId);
                if (existingQuestionOpt.isPresent()) {
                    QuestionDataTemp existingQuestion = existingQuestionOpt.get();
                    existingQuestion.setGenAiConversionCompleted(true);
                    questionDataTempRepository.save(existingQuestion);
                }
            }
        }

        if (errorMessages.length() > 0) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessages.toString(), null);
        } else {
            return new ApiResponse(HttpStatus.OK, "Data updated and saved successfully", null);
        }
    }


    public void processAndSaveOptions(String answerKey, int questionType, QuestionJsonDTO questionJsonDTO, int questionSubType) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionJsonDTO.getQuestionId());

        if (!existingOptions.isEmpty()) {

            switch (questionType) {
                case 3 -> processAndSaveOptionsForTypeInt(answerKey, questionJsonDTO);
                case 6 -> processAndSaveOptionsForTypeNumeric(answerKey, questionJsonDTO);
                case 7 -> processAndSaveOptionsForTypeColumnMatch(answerKey, questionJsonDTO);
                default -> processAndSaveOptionsDefault(answerKey, questionSubType, questionJsonDTO);
            }
        }
        System.out.println("Processed options successfully!");
    }

    //  Type 3: Integer options (0-9)
    public void processAndSaveOptionsForTypeInt(String answerKey, QuestionJsonDTO questionJsonDTO) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionJsonDTO.getQuestionId());

        List<String> options = IntStream.rangeClosed(0, 9)
                .mapToObj(String::valueOf)
                .toList();

        if (!existingOptions.isEmpty()) {
            for (int i = 0; i < existingOptions.size() && i < options.size(); i++) {
                OptionsDataTemp optionsDataTemp = existingOptions.get(i);
                boolean isCorrect = options.get(i).equals(answerKey); // True only for correct answer

                optionsDataTemp.setGenAiIsAnswer(isCorrect); // Update only genAiIsAnswer field
            }
            optionsDataTempRepository.saveAll(existingOptions); // Batch update
        } else {
            log.warn("No existing options found for questionId: {}", questionJsonDTO.getQuestionId());
        }
    }



    public void processAndSaveOptionsForTypeNumeric(String answerKey, QuestionJsonDTO questionJsonDTO) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionJsonDTO.getQuestionId());

        if (!existingOptions.isEmpty()) {
            // Ensure optionsLatex is populated
            //addOptionsToLatex(questionJsonDTO);

            // Convert list to a comma-separated string
            //   String optionsLatexStr = String.join(", ", questionJsonDTO.getOptionsLatex());

            for (OptionsDataTemp optionsDataTemp : existingOptions) {
                optionsDataTemp.setOptionLatex(" "); // Set cleaned-up optionLatex
                optionsDataTemp.setGenAiAnswer(answerKey);
            }

            optionsDataTempRepository.saveAll(existingOptions);
        } else {
            log.warn("No existing options found for questionId: {}", questionJsonDTO.getQuestionId());
        }
    }

    //  Type 7: Column Matching (A-p, B-q, C-r, D-s)

    public void processAndSaveOptionsForTypeColumnMatch(String answerKey, QuestionJsonDTO questionJsonDTO) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionJsonDTO.getQuestionId());

        if (existingOptions.isEmpty()) {
            log.warn("No existing options found for questionId: {}", questionJsonDTO.getQuestionId());
            return;
        }

        // Extract options from questionJsonDTO
        Map<String, String> optionsMap = questionJsonDTO.getOptions();
        if (optionsMap == null || optionsMap.isEmpty()) {
            log.warn("No options found in questionJsonDTO for questionId: {}", questionJsonDTO.getQuestionId());
            return;
        }

        addOptionsToLatex(questionJsonDTO); // Process LaTeX if needed

        // Convert "{A=, B=q, C=, D=p}" into ordered list of values
        answerKey = answerKey.replaceAll("[{}]", ""); // Remove `{}` brackets if present
        String[] pairs = answerKey.split(",\\s*"); // Split by `,` and trim spaces
        List<String> answerValues = new ArrayList<>();

        for (String pair : pairs) {
            String[] parts = pair.split("="); // Split "A=" into ["A", ""]
            if (parts.length == 2) {
                String value = parts[1].trim();
                if (!value.isEmpty()) {
                    answerValues.add(value); // Store only non-empty values
                }
            }
        }

        // Ensure we have enough answers to distribute
        if (answerValues.isEmpty()) {
            log.warn("No valid answer values found in answerKey: {}", answerKey);
            return;
        }

        int index = 0;
        for (OptionsDataTemp optionsDataTemp : existingOptions) {
            if (index < answerValues.size()) {
                optionsDataTemp.setGenAiAnswer(answerValues.get(index)); // Assign p, r, s, q one by one
                optionsDataTemp.setGenAiIsAnswer(true); // Always set as true
                log.info("Set genAiAnswer={} for optionText={}", optionsDataTemp.getGenAiAnswer());
                index++;
            } else {
                log.warn("Not enough answer values for all options.");
                break;
            }
        }

        optionsDataTempRepository.saveAll(existingOptions); // Batch update
    }


    // Default: Type A, B, C, D

    public void processAndSaveOptionsDefault(String answerKey, int questionSubType, QuestionJsonDTO questionJsonDTO) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionJsonDTO.getQuestionId());

        Map<String, String> optionsMap = questionJsonDTO.getOptions(); // Extract options as a map
        List<String> optionLabels = Arrays.asList("A", "B", "C", "D"); // Standard option labels

        // Convert answerKey into a Set (handles both single & multiple correct answers)
        Set<String> correctAnswers = Arrays.stream(answerKey.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        if (!existingOptions.isEmpty() && optionsMap != null) {
            for (int i = 0; i < existingOptions.size(); i++) {
                OptionsDataTemp optionsDataTemp = existingOptions.get(i);
                String optionLabel = optionLabels.get(i); // "A", "B", "C", or "D"
                String optionText = optionsMap.getOrDefault(optionLabel, ""); // Get option text safely

                // Check correctness based on questionSubType:
                boolean isCorrect;
                if (questionSubType == 1) {
                    // Single correct answer (strict match)
                    isCorrect = answerKey.equalsIgnoreCase(optionLabel);
                } else if (questionSubType == 2) {
                    // Multiple correct answers
                    isCorrect = correctAnswers.contains(optionLabel);
                } else {
                    isCorrect = false; // Default case
                }

                optionsDataTemp.setGenAiIsAnswer(isCorrect); // Update answer flag
                optionsDataTemp.setOptionLatex(optionText); // Set option text into optionLatex
            }
            optionsDataTempRepository.saveAll(existingOptions); // Batch update
        } else {
            log.warn("No existing options found or optionsMap is null for questionId: {}", questionJsonDTO.getQuestionId());
        }
    }

    // Helper Methods
    private Map<String, String> parseAnswerKey(String answerKey) {
        Map<String, String> optionsMap = new HashMap<>();
        String[] pairs = answerKey.split(","); // Split by comma

        for (String pair : pairs) {
            String[] parts = pair.split("-"); // Split A-p
            if (parts.length == 2) {
                optionsMap.put(parts[0].trim(), parts[1].trim()); // Store as (A → p)
            }
        }
        return optionsMap;
    }

    private boolean checkIfCorrectAnswer(String option, String answerKey) {
        // Extract correct answer (e.g., "C-r" → "C")
        String correctOption = answerKey.split(",")[0].split("-")[0].trim();
        return option.equalsIgnoreCase(correctOption);
    }

    public void addOptionsToLatex(QuestionJsonDTO questionJsonDTO) {
        if (questionJsonDTO.getOptions() == null || questionJsonDTO.getOptions().isEmpty()) {
            log.warn("No options found for questionId: {}", questionJsonDTO.getQuestionId());
            return;
        }

        // Initialize optionsLatex list
        List<String> optionsLatexList = new ArrayList<>();

        // Loop through the options map and add values to optionsLatex
        for (Map.Entry<String, String> entry : questionJsonDTO.getOptions().entrySet()) {
            optionsLatexList.add(entry.getValue()); // Adding option values
        }

        // Set the list in DTO
        questionJsonDTO.setOptionsLatex(optionsLatexList);

        log.info("OptionsLatex updated for questionId {}: {}", questionJsonDTO.getQuestionId(), optionsLatexList);
    }


    private String processNumericOptions(List<OptionsDataTemp> optionsDataList) {
        String[] optionLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder optionsFormatted = new StringBuilder();
        String correctAnswer = "";

        for (int i = 0; i < Math.min(optionsDataList.size(), 10); i++) {
            String option = optionLabels[i] + "-" + optionsDataList.get(i);
            optionsFormatted.append(option).append(", ");
            if (Boolean.TRUE.equals(optionsDataList.get(i).getIsAnswer())) {
                correctAnswer = optionLabels[i];
            }
        }

        return correctAnswer;
    }

    private String processNumericAnswer(List<OptionsDataTemp> optionsDataList) {
        for (OptionsDataTemp optionData : optionsDataList) {
            if (optionData.getAnswer() != null) {
                return optionData.getAnswer().trim();
            }
        }
        return "N/A";
    }

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

    private String processMultipleChoiceOptions(List<OptionsDataTemp> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        StringBuilder optionsFormatted = new StringBuilder();
        Set<String> correctAnswersSet = new HashSet<>();

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            String option = optionLabels[i] + "-" + optionsDataList.get(i);
            optionsFormatted.append(option).append(", ");

            if (Boolean.TRUE.equals(optionsDataList.get(i).getIsAnswer())) {
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
        String apiUrl = "http://13.202.253.67:5000/getSolution";

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


                //  final QuestionJsonDTO questionJsonDTO = objectMapper.readValue(response.getBody(), QuestionJsonDTO.class);
                //   log.info("Object: {}", questionJsonDTO);
                return questionJsonDTOsss;
//            } else {
//                System.out.println("Failed to retrieve solution. Status Code: " + response.getStatusCode());
//                return null;
//            }
//        } catch (Exception e) {
//            System.out.println("Error during API call: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
            } else {
                throw new CustomApiException("Failed to retrieve solution. Status Code: " + response.getStatusCode());
            }

        } catch (HttpServerErrorException e) {
            throw new CustomApiException("Internal Server Error (500) from API: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new CustomApiException("Error calling external API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomApiException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private void updateOptionsDataTemp(Long questionId, String answerKey, int questionType, QuestionJsonDTO
            questionJsonDTO) {
        List<OptionsDataTemp> existingOptions = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);

        if (existingOptions.isEmpty()) {
            log.warn("No existing options found for questionId: {}", questionId);
            return;
        }

        for (OptionsDataTemp option : existingOptions) {
            boolean isCorrect = option.getOptionLatex().equalsIgnoreCase(answerKey);

            option.setGenAiAnswer(answerKey);
            option.setGenAiIsAnswer(isCorrect);
        }

        optionsDataTempRepository.saveAll(existingOptions);
    }

    private void updateSolutionDataTemp(Long questionId, String solutionLatex) {
        SolutionDataTemp existingSolution = solutionDataTempRepository.fetchSolutionForQuestionId(questionId);

        if (existingSolution != null) {
            existingSolution.setSolutionLatex(solutionLatex);
            solutionDataTempRepository.save(existingSolution);
        } else {
            log.warn("No existing solution found for questionId: {}", questionId);
        }
    }


    public ApiResponse checkGenAiConversionStatus() {
        Double count = questionDataTempRepository.countCompletedGenAiConversions();

        if (count == null || count == 0) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "No progress data available.", null);
        }

        return new ApiResponse(HttpStatus.OK, "Gen AI conversion question count", count);
    }

}
