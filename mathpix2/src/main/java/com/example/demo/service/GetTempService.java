package com.example.demo.service;

import com.example.demo.model.OptionsData;
import com.example.demo.model.OptionsDataTemp;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.SolutionDataTemp;
import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.repository.OptionsDataTempRepository;
import com.example.demo.repository.QuestionDataTempRepository;
import com.example.demo.repository.SolutionDataTempRepository;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.resp_dto.OptionTempDataDTO;
import com.example.demo.resp_dto.QuestionTempDataDTO;
import com.example.demo.resp_dto.TempQuestionCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GetTempService {
    @Autowired
    private OptionsDataRepository optionsDataRepository;
    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private OptionsDataTempRepository optionsDataTempRepository;
    @Autowired
    private SolutionDataTempRepository solutionDataTempRepository;

    public ApiResponse getDataFromTempList() {
        List<QuestionDataTemp> questionTempData = questionDataTempRepository.getQuestionTempData();
        TempQuestionCount tempQuestionCount = new TempQuestionCount();
        if (questionTempData.isEmpty()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "No questions found", null);
        }

        List<QuestionTempDataDTO> processedData = new ArrayList<>();

        for (QuestionDataTemp questionDataTemp : questionTempData) {
            Long questionId = questionDataTemp.getQuestionId();
            Integer questionType = questionDataTemp.getQuestionType();

            List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
            log.info("Options data for questionId {}: {}", questionId, optionsDataTempList.size());
            // Fetch options only if the type is NOT 3 or 6
            List<OptionTempDataDTO> optionDataList = (questionType == 3 || questionType == 6)
                    ? new ArrayList<>()
                    : fetchOptionsByQuestionId(questionId);
            // Determine correct answer based on question type
            String correctAnswer;
            switch (questionType) {
                case 3 -> correctAnswer = processNumericOptionsForGet(optionsDataTempList);
                case 6 -> correctAnswer = processNumericAnswerForGet(optionsDataTempList);
                case 7 -> correctAnswer = processType7(optionsDataTempList, questionType);
                default -> correctAnswer = processMultipleChoiceOptionsForGet(optionsDataTempList, questionType);
            }


            SolutionDataTemp solutionForQuestion = solutionDataTempRepository.getSolutionForQuestion(questionId);

            // Construct DTO object
            QuestionTempDataDTO questionDTO = new QuestionTempDataDTO();
            questionDTO.setQuestionId(questionId);
            questionDTO.setQuestionLatex(questionDataTemp.getQuestionLatex());
            questionDTO.setQuestionImageProcessedUrl(questionDataTemp.getQuestionImageProcessedUrl() != null ? questionDTO.getQuestionImageProcessedUrl() : " ");
            questionDTO.setOptions(optionDataList);
            questionDTO.setSolutionLatex(solutionForQuestion.getSolutionLatex());
            questionDTO.setSolutionImageProcessedUrl(solutionForQuestion.getSolutionImageProcessedUrl() != null ? solutionForQuestion.getSolutionImageProcessedUrl() : " ");
            questionDTO.setComplexity(questionDataTemp.getComplexity());
            questionDTO.setAnswerKey(correctAnswer);
            questionDTO.setAnswerKeyMatched(questionDataTemp.getIsAnsMatch());

            processedData.add(questionDTO);


            tempQuestionCount.setQuestionCount(processedData.size());
            tempQuestionCount.setQuestionList(processedData);

        }

        return new ApiResponse(HttpStatus.OK, "Questions retrived successfully", tempQuestionCount);
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
