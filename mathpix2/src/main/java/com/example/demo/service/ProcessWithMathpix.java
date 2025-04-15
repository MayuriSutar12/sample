package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.input_dto.ProcessImageUrlDto;
import com.example.demo.model.QuestionInfoMathPix;
import com.example.demo.projectionInterface.QuestionSolutionInterface;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.resp_dto.Options;
import com.example.demo.resp_dto.ProcessImageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
public class ProcessWithMathpix {
    private static final String MATHPIX_API_URL_IMAGE = "https://api.mathpix.com/v3/text";

    private static final String APP_ID = "mann0707_gmail_com_fb0c0f_d77973";
    private static final String APP_KEY = "4b37b802073f9a3c06b70f9f74b7455f967808619e30e1835b1b8f8328d30583";

    public ProcessImageResponse processImageUrl(ProcessImageUrlDto processImageUrlDto) {
        try {
            Long questionId = processImageUrlDto.getQuestionId();
            String questionImageUrl = processImageUrlDto.getQuestionUrl();
            String solutionImageUrl = processImageUrlDto.getSolutionUrl();
            Integer questionType = processImageUrlDto.getQuestionType();

            if ((questionImageUrl == null || questionImageUrl.isEmpty()) &&
                    (solutionImageUrl == null || solutionImageUrl.isEmpty())) {
                throw new NotFoundException("Both Question Image URL and Solution Image URL are missing");
            }
            if (questionImageUrl == null || questionImageUrl.isEmpty()) {
                throw new NotFoundException("Question image URL is missing");
            }
            if (solutionImageUrl == null || solutionImageUrl.isEmpty()) {
                throw new NotFoundException("Solution image URL is missing");
            }

            ProcessImageResponse processImageResponse = new ProcessImageResponse();
            List<Options> options = new ArrayList<>();

            HttpHeaders headers = createHeaders();

            if (questionType == 3 || questionType == 6) {
                String questionResult = processImageWithMathpix(questionImageUrl, headers);
                Map<String, Object> questionData = separateQuestionAndOptions(questionResult);
                String extractedQuestionText = (String) questionData.get("question");

                processImageResponse.setQuestionText(extractedQuestionText);
                processImageResponse.setOptions(new ArrayList<>());

                String solutionResult = processImageWithMathpix(solutionImageUrl, headers);
                processImageResponse.setSolutionText(solutionResult);

                return processImageResponse;

            } else {
                String questionResult = processImageWithMathpix(questionImageUrl, headers);
                Map<String, Object> questionData = separateQuestionAndOptions(questionResult);
                String extractedQuestionText = (String) questionData.get("question");
                List<String> optionList = (List<String>) questionData.get("options");

                for (String optionText : optionList) {
                    options.add(Options.builder().optionText(optionText).build());
                }

                processImageResponse.setQuestionText(extractedQuestionText);
                processImageResponse.setOptions(options);

                String solutionResult = processImageWithMathpix(solutionImageUrl, headers);
                processImageResponse.setSolutionText(solutionResult);

                return processImageResponse;
            }

        } catch (NotFoundException ex) {
            return null;
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

//        // Regex to match LaTeX formulas (e.g., \(...\) or \[...\])
//        String formulaPattern = "\\\\([^)]*\\\\)|\\\\\\[(.*?)\\\\\\]";
        // Regex to match LaTeX formulas (e.g., \(...\) or \[...\]) and simple math expressions like 1/(X-4)
        //String formulaPattern = "(\\\\[\\(\\[].*?\\\\[\\)\\]]|[\\d\\w]+\\/\\([A-Za-z0-9-]+\\))";
        //  String formulaPattern = "\\\\([^)]*\\\\)|\\\\\\[(.*?)\\\\\\]";
        String formulaPattern = "(\\\\[\\(\\[].*?\\\\[\\)\\]]|[\\d\\w]+\\/\\([A-Za-z0-9-]+\\)|\\\\([^)]*\\\\)|\\\\\\[(.*?)\\\\\\])";

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
        String[] lines = text.split("\n");

        boolean isOption = false;
        boolean foundRecentOptions = false;

        // Loop through lines to separate question and options
        for (String line : lines) {
            line = line.trim(); // Remove leading and trailing spaces

            // Match options starting with (A), (B), (C), etc.
            //  if (line.matches("^\\([A-Da-d]\\).*|^[A-Da-d]\\).*|^\\(\\d\\).*|^\\d\\).*")) {

            // if (line.matches("^(\\([A-Za-z0-9]\\)|^[A-Za-z0-9]\\)\\s+.*$|\\d+\\.\\s+.*)")) {
            //  if (line.matches("^(\\([A-Za-z]\\)|^[A-Za-z]\\)\\s+.*$|^[a-dA-D]\\)\\s+.*$|^\\d+\\)\\s+.*$|^\\d+\\.\\s+.*$|^\\([A-Da-d]\\)\\s+.*$)")) {

            // if (line.matches("^\\([A-Da-d]\\).*|^[A-Da-d]\\).*|^\\(\\d\\).*|^\\d\\).*")) {

            // if (line.matches("^\\([A-D]\\).*")) {
            if (line.matches(".*\\([A-Da-d]\\).*") ||
                    line.matches("^(\\([A-Za-z]\\)|^[A-Za-z]\\)\\s+.*$|^[a-dA-D]\\)\\s+.*$|^\\d+\\)\\s+.*$|^\\d+\\.\\s+.*$|^\\([A-Da-d]\\)\\s+.*$)")) {

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
                    question.append(line).append(" "); // Continue appending to question
                    // System.out.printf("append que  " + question);
                    // }
                } else if (!foundRecentOptions) {
                    // If in earlier options block but not recent (A, B, C, D)
                    previousOptions.add(line);
                    System.out.printf("prevoius one" + previousOptions);
                }
            }
        }

        Collections.sort(previousOptions);
        // Append previous options (P, Q, R, S) after current options (A, B, C, D) in the question text
        StringBuilder combinedOptions = new StringBuilder();

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
            // Remove numeric labels like 1., 2., 3., 4., (1), (2), (3), (4)
            String optionWithoutLabel = option.replaceAll("^[1-4]\\.?\\s?\\(?[1-4a-zA-z]*\\)?\\s?", "");
            // String optionWithoutLabel = option.replaceAll("^(\\(?[A-Da-d1-4]\\)?[\\s\\.)]*)", "").trim();

            // Remove letter labels like (A), (B), (C), (D), or a), b), c), d) etc.
            optionWithoutLabel = optionWithoutLabel.replaceAll("^(\\(?[A-Za-z]\\)?[\\s\\.)]*)", "").trim();

            // Replace the placeholder formulas in the option
            String optionWithFormulas = optionWithoutLabel;
            for (int i = 0; i < formulas.size(); i++) {
                optionWithFormulas = optionWithFormulas.replace("__FORMULA_" + (i + 1) + "__", formulas.get(i));
            }

            // Add the processed option to the list
            optionsWithFormulas.add(optionWithFormulas);
        }

        // Prepare result
        Map<String, Object> result = new HashMap<>();
        result.put("question", questionWithFormulas.trim());
        result.put("options", optionsWithFormulas);

        return result;
    }

}
