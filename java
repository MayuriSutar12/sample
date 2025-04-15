package com.example.demo.service;

import com.example.demo.model.OptionsData;
import com.example.demo.model.OptionsDataMathPix;
import com.example.demo.model.QuestionInfoMathPix;
import com.example.demo.model.SolutionMathPix;
import com.example.demo.projectionInterface.QuestionSolutionInterface;
import com.example.demo.repository.OptionsDataMathPixRepository;
import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.repository.QuestionInfoMathPixRepository;
import com.example.demo.repository.SolutionMathPixRepository;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.resp_dto.Options;
import com.example.demo.resp_dto.ProcessImageResponse;
import com.example.demo.input_dto.SaveQuestionDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MathPixNewService {
    private static final String MATHPIX_API_URL_IMAGE = "https://api.mathpix.com/v3/text";

    private static final String MATHPIX_API_URL_PDF = "https://api.mathpix.com/v3/pdf";

    private static final String MATHPIX_PDF_CONVERTER_API_URL = "https://api.mathpix.com/v3/converter";

    private static final String APP_ID = "mann0707_gmail_com_fb0c0f_d77973";
    private static final String APP_KEY = "4b37b802073f9a3c06b70f9f74b7455f967808619e30e1835b1b8f8328d30583";

    @Autowired
    QuestionInfoMathPixRepository questionInfoMathPixRepository;
    @Autowired
    OptionsDataMathPixRepository optionsDataMathPixRepository;
    @Autowired
    SolutionMathPixRepository solutionMathPixRepository;
    @Autowired
    OptionsDataRepository optionsDataRepository;


    public ApiResponse processImageUrl(Integer questionId, String questionImageUrl, String solutionImageUrl) {
        try {
            // Validate input URLs
            if (questionImageUrl == null || questionImageUrl.isEmpty() || solutionImageUrl == null || solutionImageUrl.isEmpty()) {
                return new ApiResponse(HttpStatus.BAD_REQUEST, "Both questionImageUrl and solutionImageUrl must exist and cannot be empty.", null);
            }
            List<QuestionSolutionInterface> rows = questionInfoMathPixRepository.findQuestionSolutionById(questionId);
            if (!rows.isEmpty()) {
                // Extract question and solution data (same for all rows)
                String questionText = rows.get(0).getQuestionText();
                String questionImageUrls = rows.get(0).getQuestionImageUrl();
                String solutionText = rows.get(0).getSolutionText();
                String solutionImageUrls = rows.get(0).getSolutionImageUrl();


                // Extract and group options
                List<Options> options = rows.stream()
                        .map(row -> Options.builder()
                                .optionText(row.getOptionText())
                                .optionImgUrl(row.getOptionImgUrl())
                                .build())
                        .toList();

                // Build the final response
                ProcessImageResponse processImageResponse = ProcessImageResponse.builder()
                        .questionText(questionText)
                        .questionImageUrl(questionImageUrls)
                        .options(options)
                        .solutionText(solutionText)
                        .solutionImageUrl(solutionImageUrls)
                        .build();

                return new ApiResponse(HttpStatus.OK, "Process successfully", processImageResponse);


            } else {
                // Create headers
                HttpHeaders headers = createHeaders();

                // Process question URL
                String questionResult = processImageWithMathpix(questionImageUrl, headers);

                // Separate question and options
                Map<String, Object> questionData = separateQuestionAndOptions(questionResult);

                // Build the options list
                List<Options> options = new ArrayList<>();
                // Assuming options are extracted from questionData
                List<String> optionList = (List<String>) questionData.get("options");
                for (String optionText : optionList) {
                    Options option = new Options();
                    option.setOptionText(optionText);
                    // You can add logic for option images if available
                    option.setOptionImgUrl("");  // Set the image URL if available
                    options.add(option);
                }

                // Build the response object
                ProcessImageResponse processImageResponse = new ProcessImageResponse();
                processImageResponse.setQuestionText((String) questionData.get("question"));
                processImageResponse.setQuestionImageUrl("");

                processImageResponse.setOptions(options);

                // Process solution URL
                String solutionResult = processImageWithMathpix(solutionImageUrl, headers);

                // Add the solution text and image URL to the response
                processImageResponse.setSolutionText(solutionResult);
                processImageResponse.setSolutionImageUrl("");

                // Return a custom ApiResponse with the processed data
                return new ApiResponse(HttpStatus.OK, "Process successfully", processImageResponse);
            }
        } catch (Exception e) {
            log.error("Error while processing image with Mathpix API", e);
            throw new RuntimeException("Error while processing image with Mathpix API: " + e.getMessage());
        }
    }

    private String processImageWithMathpix(String imageUrl, HttpHeaders headers) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("url", imageUrl);
            requestBody.put("formats", new String[]{"text", "latex_simplified"});
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(MATHPIX_API_URL_IMAGE, entity, String.class);
            String result = response.getBody();
            log.info("Mathpix result for URL {}: {}", imageUrl, result);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            return jsonNode.path("text").asText();
        } catch (Exception e) {
            log.error("Error while processing image URL with Mathpix API", e);
            throw new RuntimeException("Error while processing image URL: " + e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("app_id", APP_ID);
        headers.set("app_key", APP_KEY);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public Map<String, Object> separateQuestionAndOptions(String text) {
        // Initialize StringBuilder for the question and lists for options and formulas
        StringBuilder question = new StringBuilder();
        List<String> options = new ArrayList<>();
        List<String> previousOptions = new ArrayList<>();
        List<String> formulas = new ArrayList<>();

        // Regex to match LaTeX formulas (e.g., \(...\) or \[...\])
        String formulaPattern = "\\\\([^)]*\\\\)|\\\\\\[(.*?)\\\\\\]";

        // Replace formulas with placeholders and store them
        Pattern pattern = Pattern.compile(formulaPattern);
        Matcher matcher = pattern.matcher(text);
        int formulaIndex = 1;

        while (matcher.find()) {
            String formula = matcher.group(); // Extract the formula
            formulas.add(formula); // Store the formula in a list
            text = text.replace(formula, "__FORMULA_" + formulaIndex++ + "__"); // Replace with a placeholder
        }

        // Remove additional LaTeX formatting
        text = text.replaceAll("\\\\\\[.*?\\\\\\]", "") // Remove block-level LaTeX
                .replaceAll("\\\\[a-zA-Z]+\\{[^}]*\\}", "") // Remove inline LaTeX commands
                .replaceAll("[^\\x00-\\x7F]+", "") // Remove non-ASCII characters
                .trim();

        // Split the text into lines
        //String[] lines = text.split("\n");
        String[] lines = text.split("\n");

        boolean isOption = false;
        boolean foundRecentOptions = false;

        // Loop through lines to separate question and options
        for (String line : lines) {
            line = line.trim(); // Remove leading and trailing spaces

            // Match options starting with (A), (B), (C), etc.
            if (line.matches("^\\([A-Da-d]\\).*|^[A-Da-d]\\).*|^\\(\\d\\).*|^\\d\\).*")) {
                // if (line.matches("^\\([A-D]\\).*")) {
                // If recent options group (A, B, C, D) starts
                if (line.matches("^\\([A-Da-d]\\).*")) {
                    if (!foundRecentOptions) {
                        foundRecentOptions = true;
                        previousOptions.addAll(options); // Move earlier options to previousOptions
                        System.out.printf("all options" + previousOptions);
                        options.clear(); // Start fresh for recent options
                    }
                }
                options.add(line); // Add the current line to options
                isOption = true;
            } else {


//                // Before options start, add lines to the question
                if (!isOption) {
//                    question.append(line).append(" ");

                    // If there's a question mark at the end of the line or words, treat it as the question's end
                    if (line.endsWith(".*\\?[A-Za-z]\\s*$\n")) {
                        question.append(line).append(" ");
                        System.out.printf("line end" + question);
                        break;
                    } else {
                        question.append(line).append(" "); // Continue appending to question
                        System.out.printf("append que  " + question);
                    }
                } else if (!foundRecentOptions) {
                    // If in earlier options block but not recent (A, B, C, D)
                    previousOptions.add(line);
                    System.out.printf("prevoius one" + previousOptions);
                }
            }
        }

//        // Append previous options (P, Q, R, S) after current options (A, B, C, D) in the question text
//        StringBuilder combinedOptions = new StringBuilder();
//        for (String previousOption : previousOptions) {
//            combinedOptions.append(previousOption).append(" ");
//        }
//
//        question.append(combinedOptions); // Add previous options to the question text
//        System.out.printf("combiled options "+question);
        Collections.sort(previousOptions);
        // Append previous options (P, Q, R, S) after current options (A, B, C, D) in the question text
        StringBuilder combinedOptions = new StringBuilder();

//        // Add A, B, C, D options first
//        for (String option : options) {
//            combinedOptions.append(option).append(" ");
//        }

        // Add P, Q, R, S options next
        for (String previousOption : previousOptions) {
            combinedOptions.append(previousOption).append(" ");
        }

        // Add combined options to the question
        question.append(combinedOptions);
        // Replace formula placeholders with actual formulas in the question and options
        String questionWithFormulas = question.toString();
        for (int i = 0; i < formulas.size(); i++) {
            questionWithFormulas = questionWithFormulas.replace("__FORMULA_" + (i + 1) + "__", formulas.get(i));
        }

        List<String> optionsWithFormulas = new ArrayList<>();
        for (String option : options) {
            String optionWithFormulas = option;
            for (int i = 0; i < formulas.size(); i++) {
                optionWithFormulas = optionWithFormulas.replace("__FORMULA_" + (i + 1) + "__", formulas.get(i));
            }
            optionsWithFormulas.add(optionWithFormulas);
        }


        // Prepare result
        Map<String, Object> result = new HashMap<>();
        result.put("question", questionWithFormulas.trim());
        result.put("options", optionsWithFormulas);
        System.out.printf("questions==" + questionWithFormulas);
        System.out.printf("options===" + optionsWithFormulas);


        return result;
    }


    public ApiResponse saveMathpixQuestion(SaveQuestionDTO saveQuestionDTO) {
        try {
            // Validate input data
            validateInputData(saveQuestionDTO);

            Integer questionId = saveQuestionDTO.getQuestionId();
            String questionText = saveQuestionDTO.getQuestionText();
            String questionUrl = saveQuestionDTO.getQuestionUrl();
            List<Options> options = saveQuestionDTO.getOptions();
            String solutionText = saveQuestionDTO.getSolutionText();
            String solutionImage = saveQuestionDTO.getSolutionImage();
            log.info("in method of save");
            // Check if question ID exists
            QuestionInfoMathPix questionExists = questionInfoMathPixRepository.findByQuestionId(questionId);

            System.out.printf("questionId prsent---------" + questionExists);

            if (questionExists != null) {
               System.out.printf("in update loop");
                updateExistingQuestion(questionId, questionText, questionUrl, options, solutionText, solutionImage);
                return new ApiResponse(HttpStatus.OK, "Question updated successfully!", null);
            } else {
                System.out.printf("in update else loop");
                saveNewQuestion(questionId, questionText, questionUrl, options, solutionText, solutionImage);
                return new ApiResponse(HttpStatus.OK, "Question saved successfully!", null);
            }
        } catch (IllegalArgumentException e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while saving the question.", null);
        }
    }

    private void validateInputData(SaveQuestionDTO saveQuestionDTO) {
        if (saveQuestionDTO.getQuestionText() == null || saveQuestionDTO.getQuestionText().isEmpty()) {
            throw new IllegalArgumentException("Question text must not be null or empty.");
        }
        if (saveQuestionDTO.getOptions() == null || saveQuestionDTO.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Options must not be null or empty.");
        }
        if (saveQuestionDTO.getSolutionText() == null || saveQuestionDTO.getSolutionText().isEmpty()) {
            throw new IllegalArgumentException("Solution text must not be null or empty.");
        }
    }


    @Transactional
    public void updateExistingQuestion(Integer questionId, String questionText, String questionUrl,
                                       List<Options> options, String solutionText, String solutionImage) {
        // Retrieve the existing question from the repository
        log.info("Updating question...");
        QuestionInfoMathPix existingQuestion = questionInfoMathPixRepository.findByQuestionId(questionId);

        // Update the fields of the existing question entity
        existingQuestion.setQuestionText(questionText);
        existingQuestion.setQuestionImageUrl(questionUrl);

        // Save the updated question
        questionInfoMathPixRepository.save(existingQuestion); // Save


        // Fetch all options for the given questionId
        List<OptionsDataMathPix> existingOptions = optionsDataMathPixRepository.findOptionsByQuestionId(questionId);

        if (existingOptions == null || existingOptions.isEmpty()) {
            throw new IllegalArgumentException("No options found for question ID " + questionId);
        }

        // Iterate through the options and update
        for (int i = 0; i < options.size(); i++) {
            Options inputOption = options.get(i);

            if (i < existingOptions.size()) {
                OptionsDataMathPix existingOption = existingOptions.get(i);

                // Debug the input data
                System.out.println("Input Option Text: " + inputOption.getOptionText());
                System.out.println("Input Option Image URL: " + inputOption.getOptionImgUrl());

                // Update the fields
                existingOption.setOptionText(inputOption.getOptionText());
                existingOption.setOptionImageUrl(inputOption.getOptionImgUrl());

                // Debug the updated fields
                System.out.println("Updated Option Text: " + existingOption.getOptionText());
                System.out.println("Updated Option Image URL: " + existingOption.getOptionImageUrl());

                // Save the updated option
                optionsDataMathPixRepository.save(existingOption);
            } else {
                // Log for missing existing option
                System.out.println("No existing option to update for input option at index: " + i);
            }
        }

        // Update the solution associated with the question if it exists
        Optional<SolutionMathPix> existingSolutionOpt = solutionMathPixRepository.findByQuestionId(questionId);
        System.out.printf("Existing solution: " + existingSolutionOpt);

        if (existingSolutionOpt.isPresent()) {
            // Update the existing solution
            SolutionMathPix existingSolution = existingSolutionOpt.get();

            // Update solution text and image URL
            existingSolution.setSolutionText(solutionText);  // Corrected typo here
            existingSolution.setSolutionImageUrl(solutionImage);

            // Save the updated solution (this will perform an UPDATE operation)
            solutionMathPixRepository.save(existingSolution);
            System.out.println("Updated existing solution for question ID: " + questionId);
        } else {
            // If no solution exists, log it or handle it as needed
            System.out.println("No existing solution for question ID: " + questionId);
        }

    }

    
    private void saveNewQuestion(Integer questionId, String questionText, String questionUrl,
                                 List<Options> options, String solutionText, String solutionImage) {
        // Create a new QuestionInfoMathPix entity and set the fields
        QuestionInfoMathPix newQuestion = new QuestionInfoMathPix();
        newQuestion.setQuestionId(questionId);
        newQuestion.setQuestionText(questionText);
        newQuestion.setQuestionImageUrl(questionUrl);

        // Save the new question (this will perform an INSERT operation)
        QuestionInfoMathPix savedQuestion = questionInfoMathPixRepository.save(newQuestion);
        System.out.println("Question saved with ID: " + savedQuestion.getQuestionId());

        // Save options associated with the new question
        List<String> optionTexts = new ArrayList<>();
        List<String> optionImageUrls = new ArrayList<>();

        // Fetch the options associated with the given questionId
        List<OptionsData> fetchedOptions = optionsDataRepository.fetchOptionsById(questionId.longValue());
        log.info("Fetched options: " + fetchedOptions);

        // Check if the size of the fetched options and input options match
        if (options.size() != fetchedOptions.size()) {
            throw new IllegalArgumentException("The number of options provided does not match the number of fetched options.");
        }

        // Iterate through the options and add them to the new question
        for (int i = 0; i < options.size(); i++) {
            // Get the input option from the list
            Options option = options.get(i);

            // Get the corresponding fetched option
            OptionsData fetchedOption = fetchedOptions.get(i);

            // Create a new OptionsDataMathPix object and set its fields
            OptionsDataMathPix newOption = new OptionsDataMathPix();

            // Set optionId based on input option or fetched option
            newOption.setOptionId( fetchedOption.getOptions_id().intValue());

            // Set option text and image URL from the input options
            newOption.setOptionText(option.getOptionText());  // Take from input
            newOption.setOptionImageUrl(option.getOptionImgUrl());  // Take from input

            // Associate with the saved question
            newOption.setQuestionInfoMathPix(savedQuestion);

            // Add option text and image URL to respective lists (for logging or other purposes)
            optionTexts.add(newOption.getOptionText());
            optionImageUrls.add(newOption.getOptionImageUrl());

            // Save the new option
            final OptionsDataMathPix save = optionsDataMathPixRepository.save(newOption);
             log.info("option data"+save);
            // Print out details of the saved option
            System.out.println("Option " + (i + 1) + " Text: " + newOption.getOptionText() +
                    ", Image URL: " + newOption.getOptionImageUrl());
        }

        // Print the lists of option texts and image URLs
        System.out.println("List of Option Texts: " + optionTexts);
        System.out.println("List of Option Image URLs: " + optionImageUrls);

        // Save the solution associated with the new question if it doesn't already exist
        if (!solutionMathPixRepository.existsByQuestionId(questionId)) {
            SolutionMathPix newSolution = SolutionMathPix.builder()
                    .solutionText(solutionText)
                    .solutionImageUrl(solutionImage)
                    .questionInfoMathPix(savedQuestion)
                    .build();

            solutionMathPixRepository.save(newSolution);
        } else {
            System.out.println("A solution already exists for Question ID: " + questionId);
        }
    }

}
