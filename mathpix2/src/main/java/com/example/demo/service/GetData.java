package com.example.demo.service;


import com.example.demo.model.OptionsDataTemp;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.SolutionDataTemp;
import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.repository.OptionsDataTempRepository;
import com.example.demo.repository.QuestionDataTempRepository;
import com.example.demo.repository.SolutionDataTempRepository;
import com.example.demo.resp_dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GetData {
    @Autowired
    private OptionsDataRepository optionsDataRepository;
    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private OptionsDataTempRepository optionsDataTempRepository;
    @Autowired
    private SolutionDataTempRepository solutionDataTempRepository;

    // ... other autowired fields and methods

//    // Method to separate LaTeX and text content
//    public static List<ContentBlock> separateContent(String content) {
//        List<ContentBlock> textContent = new ArrayList<>();
//        List<ContentBlock> latexContent = new ArrayList<>();
//
//        // Regular expression to identify LaTeX content
//        String latexPattern = "(\\\\[\\w]+\\{[^}]*\\}|\\\\[\\w]+\\[[^\\]]*\\]|\\\\[\\w]+)";
//        Pattern pattern = Pattern.compile(latexPattern);
//        Matcher matcher = pattern.matcher(content);
//
//        // Start of parsing content
//        int lastIndex = 0;
//
//        while (matcher.find()) {
//            // Text before LaTeX
//            if (matcher.start() > lastIndex) {
//                textContent.add(new ContentBlock("Text", content.substring(lastIndex, matcher.start())));
//            }
//
//            // LaTeX part
//            latexContent.add(new ContentBlock("InlineMath", matcher.group()));
//
//            // Update the last index to current LaTeX match's end
//            lastIndex = matcher.end();
//        }
//
//        // If there's any text remaining after the last LaTeX match
//        if (lastIndex < content.length()) {
//            textContent.add(new ContentBlock("Text", content.substring(lastIndex)));
//        }
//
//        // Return both Text and LaTeX content separately
//        List<ContentBlock> finalContent = new ArrayList<>();
//        finalContent.addAll(textContent);
//        finalContent.addAll(latexContent);
//
//        return finalContent;
//    }
//
//    public ApiResponse getData() {
//        List<QuestionDataTemp> questionTempData = questionDataTempRepository.getQuestionTempData();
//        TempQuestionCountNew tempQuestionCount = new TempQuestionCountNew();
//
//        if (questionTempData.isEmpty()) {
//            return new ApiResponse(HttpStatus.BAD_REQUEST, "No questions found", null);
//        }
//
//        List<QuestionTempDataDTONew> processedData = new ArrayList<>();
//
//        for (QuestionDataTemp questionDataTemp : questionTempData) {
//            Long questionId = questionDataTemp.getQuestionId();
//            Integer questionType = questionDataTemp.getQuestionType();
//
//            List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
//            log.info("Options data for questionId {}: {}", questionId, optionsDataTempList.size());
//
//            List<OptionTempDataDTO> optionDataList = (questionType == 3 || questionType == 6)
//                    ? new ArrayList<>()
//                    : fetchOptionsByQuestionId(questionId);
//
//            String correctAnswer;
//            switch (questionType) {
//                case 3 -> correctAnswer = processNumericOptionsForGet(optionsDataTempList);
//                case 6 -> correctAnswer = processNumericAnswerForGet(optionsDataTempList);
//                case 7 -> correctAnswer = processType7(optionsDataTempList, questionType);
//                default -> correctAnswer = processMultipleChoiceOptionsForGet(optionsDataTempList, questionType);
//            }
//
//            SolutionDataTemp solutionForQuestion = solutionDataTempRepository.getSolutionForQuestion(questionId);
//
//            // Separate the content into text and LaTeX for question, options, and solution
//            List<ContentBlock> questionTextContent = new ArrayList<>();
//            List<ContentBlock> questionLatexContent = new ArrayList<>();
//
//            // Process question content
//            if (questionDataTemp.getQuestionLatex() != null) {
//                List<ContentBlock> questionContent = separateContent(questionDataTemp.getQuestionLatex());
//                questionTextContent.addAll(questionContent.stream().filter(block -> block.getType().equals("Text")).toList());
//                questionLatexContent.addAll(questionContent.stream().filter(block -> block.getType().equals("InlineMath")).toList());
//            }
//
//            // Process options content
//            List<List<ContentBlock>> optionTextContents = new ArrayList<>();
//            List<List<ContentBlock>> optionLatexContents = new ArrayList<>();
//            for (OptionsDataTemp option : optionsDataTempList) {
//                if (option.getOptionLatex() != null) {
//                    List<ContentBlock> optionContent = separateContent(option.getOptionLatex());
//                    optionTextContents.add(optionContent.stream().filter(block -> block.getType().equals("Text")).toList());
//                    optionLatexContents.add(optionContent.stream().filter(block -> block.getType().equals("InlineMath")).toList());
//                }
//            }
//
//            // Process solution content
//            List<ContentBlock> solutionTextContent = new ArrayList<>();
//            List<ContentBlock> solutionLatexContent = new ArrayList<>();
//            if (solutionForQuestion != null && solutionForQuestion.getSolutionLatex() != null) {
//                List<ContentBlock> solutionContent = separateContent(solutionForQuestion.getSolutionLatex());
//                solutionTextContent.addAll(solutionContent.stream().filter(block -> block.getType().equals("Text")).toList());
//                solutionLatexContent.addAll(solutionContent.stream().filter(block -> block.getType().equals("InlineMath")).toList());
//            }
//
//            // Construct the DTO for this question
//            QuestionTempDataDTONew questionDTO = QuestionTempDataDTONew.builder()
//                    .questionId(questionId)
//                    .questionLatex(questionDataTemp.getQuestionLatex())
//                    .questionImageProcessedUrl(questionDataTemp.getQuestionImageProcessedUrl() != null ? questionDataTemp.getQuestionImageProcessedUrl() : " ")
//                    .options(optionDataList)
//                    .solutionLatex(solutionForQuestion != null ? solutionForQuestion.getSolutionLatex() : " ")
//                    .solutionImageProcessedUrl(solutionForQuestion != null && solutionForQuestion.getSolutionImageProcessedUrl() != null ? solutionForQuestion.getSolutionImageProcessedUrl() : " ")
//                    .complexity(questionDataTemp.getComplexity())
//                    .answerKey(correctAnswer)
//                    .answerKeyMatched(questionDataTemp.getIsAnsMatch())
//                    .questionTextContent(questionTextContent)
//                    .questionLatexContent(questionLatexContent)
//                    .optionTextContents(optionTextContents)
//                    .optionLatexContents(optionLatexContents)
//                    .solutionTextContent(solutionTextContent)
//                    .solutionLatexContent(solutionLatexContent)
//                    .build();
//
//            processedData.add(questionDTO);
//        }
//
//        tempQuestionCount.setQuestionCount(processedData.size());
//        tempQuestionCount.setQuestionList(processedData);
//
//        return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", tempQuestionCount);
//    }
public ApiResponse getData() {
    List<QuestionDataTemp> questionTempData = questionDataTempRepository.getQuestionTempData();
    TempQuestionCountNew tempQuestionCount = new TempQuestionCountNew();

    if (questionTempData.isEmpty()) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, "No questions found", null);
    }

    List<QuestionTempDataDTONew> processedData = new ArrayList<>();

    for (QuestionDataTemp questionDataTemp : questionTempData) {
        Long questionId = questionDataTemp.getQuestionId();
        Integer questionType = questionDataTemp.getQuestionType();

        List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
        log.info("Options data for questionId {}: {}", questionId, optionsDataTempList.size());

        List<OptionTempDataDTO> optionDataList = (questionType == 3 || questionType == 6)
                ? new ArrayList<>()
                : fetchOptionsByQuestionId(questionId);

        String correctAnswer;
        switch (questionType) {
            case 3 -> correctAnswer = processNumericOptionsForGet(optionsDataTempList);
            case 6 -> correctAnswer = processNumericAnswerForGet(optionsDataTempList);
            case 7 -> correctAnswer = processType7(optionsDataTempList, questionType);
            default -> correctAnswer = processMultipleChoiceOptionsForGet(optionsDataTempList, questionType);
        }

        SolutionDataTemp solutionForQuestion = solutionDataTempRepository.getSolutionForQuestion(questionId);

        // Process question content
        List<ContentBlock> questionContent = separateContent(questionDataTemp.getQuestionLatex());

        // Create a combined list of question content where text and LaTeX alternate line by line
        List<ContentBlock> combinedContent = new ArrayList<>();
        boolean isLatex = false; // Start with text
        for (ContentBlock block : questionContent) {
            if (block.getType().equals("Text") && !isLatex) {
                combinedContent.add(block);
                isLatex = true;  // Next, we will insert LaTeX
            } else if (block.getType().equals("InlineMath") && isLatex) {
                combinedContent.add(block);
                isLatex = false;  // Next, we will insert Text
            }
        }

        // Process options
        List<List<ContentBlock>> optionTextContents = new ArrayList<>();
        List<List<ContentBlock>> optionLatexContents = new ArrayList<>();
        for (OptionsDataTemp option : optionsDataTempList) {
            if (option.getOptionLatex() != null) {
                List<ContentBlock> optionContent = separateContent(option.getOptionLatex());
                List<ContentBlock> combinedOptionContent = new ArrayList<>();
                boolean optionIsLatex = false;

                for (ContentBlock block : optionContent) {
                    if (block.getType().equals("Text") && !optionIsLatex) {
                        combinedOptionContent.add(block);
                        optionIsLatex = true;
                    } else if (block.getType().equals("InlineMath") && optionIsLatex) {
                        combinedOptionContent.add(block);
                        optionIsLatex = false;
                    }
                }

                optionTextContents.add(combinedOptionContent);
                optionLatexContents.add(combinedOptionContent);
            } else {
                optionTextContents.add(new ArrayList<>());
                optionLatexContents.add(new ArrayList<>());
            }
        }

        // Process solution content
        List<ContentBlock> solutionTextContent = new ArrayList<>();
        List<ContentBlock> solutionLatexContent = new ArrayList<>();
        if (solutionForQuestion != null && solutionForQuestion.getSolutionLatex() != null) {
            List<ContentBlock> solutionContent = separateContent(solutionForQuestion.getSolutionLatex());
            boolean solutionIsLatex = false;
            for (ContentBlock block : solutionContent) {
                if (block.getType().equals("Text") && !solutionIsLatex) {
                    solutionTextContent.add(block);
                    solutionIsLatex = true;
                } else if (block.getType().equals("InlineMath") && solutionIsLatex) {
                    solutionLatexContent.add(block);
                    solutionIsLatex = false;
                }
            }
        }

        QuestionTempDataDTONew questionDTO = QuestionTempDataDTONew.builder()
                .questionId(questionId)
                .questionLatex(questionDataTemp.getQuestionLatex())
                .questionImageProcessedUrl(questionDataTemp.getQuestionImageProcessedUrl() != null ? questionDataTemp.getQuestionImageProcessedUrl() : " ")
                .options(optionDataList)
                .solutionLatex(solutionForQuestion != null ? solutionForQuestion.getSolutionLatex() : " ")
                .solutionImageProcessedUrl(solutionForQuestion != null && solutionForQuestion.getSolutionImageProcessedUrl() != null ? solutionForQuestion.getSolutionImageProcessedUrl() : " ")
                .complexity(questionDataTemp.getComplexity())
                .answerKey(correctAnswer)
                .answerKeyMatched(questionDataTemp.getIsAnsMatch())
                .questionTextContent(combinedContent)  // Text and Latex combined as per the format.
                .optionTextContents(optionTextContents)
                .optionLatexContents(optionLatexContents)
                .solutionTextContent(solutionTextContent)
                .solutionLatexContent(solutionLatexContent)
                .build();

        processedData.add(questionDTO);
    }

    tempQuestionCount.setQuestionCount(processedData.size());
    tempQuestionCount.setQuestionList(processedData);

    return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", tempQuestionCount);
}

    public static List<ContentBlock> separateContent(String content) {
        List<ContentBlock> combinedContent = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            return combinedContent;  // Return empty list if content is null or empty
        }

        // Regular expression to match LaTeX content (inline math or LaTeX commands)
        String latexPattern = "(\\\\\\([^\\)]*\\)|\\\\[\\w]+\\{[^}]*\\}|\\\\[\\w]+\\[[^\\]]*\\]|\\\\[\\w]+)";
        Pattern pattern = Pattern.compile(latexPattern);
        Matcher matcher = pattern.matcher(content);

        int lastIndex = 0; // Tracks the end of the last match

        while (matcher.find()) {
            // If there's any text before the LaTeX content, add it as Text
            if (matcher.start() > lastIndex) {
                String textBeforeLatex = content.substring(lastIndex, matcher.start());
                if (!textBeforeLatex.isEmpty()) {
                    combinedContent.add(new ContentBlock("Text", textBeforeLatex));  // Add text block
                }
            }

            // Add LaTeX content as InlineMath
            String latex = matcher.group();
            if (!latex.isEmpty()) {
                combinedContent.add(new ContentBlock("InlineMath", latex));  // Add LaTeX block
            }

            // Update lastIndex to the end of the current LaTeX match
            lastIndex = matcher.end();
        }

        // If there's any remaining text after the last LaTeX match, add it as Text
        if (lastIndex < content.length()) {
            String remainingText = content.substring(lastIndex);
            if (!remainingText.isEmpty()) {
                combinedContent.add(new ContentBlock("Text", remainingText));  // Add remaining text
            }
        }

        return combinedContent;
    }






    // Process numeric options (0-9)
    public String processNumericOptionsForGet(List<OptionsDataTemp> optionsDataList) {
        String[] optionLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (int i = 0; i < Math.min(optionsDataList.size(), 10); i++) {
            if (Boolean.TRUE.equals(optionsDataList.get(i).getGenAiIsAnswer())) {
                return optionLabels[i];
            }
        }
        return "N/A";
    }

    // Process numeric answer (for type 6)
    public String processNumericAnswerForGet(List<OptionsDataTemp> optionsDataList) {
        for (OptionsDataTemp optionData : optionsDataList) {
            if (optionData.getAnswer() != null && !optionData.getAnswer().trim().isEmpty()) {
                String answer = optionData.getAnswer().trim();
                log.info("Numeric answer retrieved: {}", answer);
                return answer;
            }
        }
        return "N/A";
    }

    // Process multiple-choice options (A, B, C, D)
    public String processMultipleChoiceOptionsForGet(List<OptionsDataTemp> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        Set<String> correctAnswersSet = new HashSet<>();

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            if (Boolean.TRUE.equals(optionsDataList.get(i).getGenAiIsAnswer())) {
                String answerText = (optionsDataList.get(i).getAnswer() != null) ? optionsDataList.get(i).getAnswer().trim() : "N/A";
                log.info("Answer: {}", answerText);
                correctAnswersSet.add(optionLabels[i]);

            }
        }
        return String.join(",", correctAnswersSet);
    }


    public String processType7(List<OptionsDataTemp> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        Set<String> correctAnswersSet = new HashSet<>();
        List<String> allLabels = new ArrayList<>();
        List<String> allAnswers = new ArrayList<>();
        boolean allTrue = true, allFalse = true;

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            OptionsDataTemp option = optionsDataList.get(i);
            boolean isAnswer = Boolean.TRUE.equals(option.getGenAiIsAnswer());
            String answerText = (option.getGenAiAnswer() != null) ? option.getGenAiAnswer().trim() : "N/A";

            if (isAnswer) allFalse = false;
            else allTrue = false;

            allLabels.add(optionLabels[i]);
            allAnswers.add(answerText);
            //   log.info("genAi is naswer " + answerText);
            correctAnswersSet.add(optionLabels[i] + "-" + answerText);

        }
        // Otherwise, return "A-q, B-q, C-s, D-p"
        return String.join(",", correctAnswersSet);

    }

    //fetch options list
    public List<OptionTempDataDTO> fetchOptionsByQuestionId(Long questionId) {
        List<OptionTempDataDTO> optionDataList = new ArrayList<>();

        List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
        log.info("Options data for questionId {}: {}", questionId, optionsDataTempList.size());

        for (OptionsDataTemp option : optionsDataTempList) {
            OptionTempDataDTO optionDTO = new OptionTempDataDTO();
            optionDTO.setOptionLatex(option.getOptionLatex() != null ? option.getOptionLatex() : " ");
            optionDTO.setOptionImageProcessedUrl(option.getOptionImageProcessedUrl() != null ? option.getOptionImageProcessedUrl() : " ");
            optionDataList.add(optionDTO);
        }

        return optionDataList;
    }

}
