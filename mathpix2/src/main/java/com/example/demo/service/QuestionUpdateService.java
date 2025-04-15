package com.example.demo.service;

import com.example.demo.exception.MissingDataException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.input_dto.UpdateTempQuestionDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.resp_dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionUpdateService {
    @Autowired
    private OptionsDataRepository optionsDataRepository;
    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private OptionsDataTempRepository optionsDataTempRepository;
    @Autowired
    private SolutionDataTempRepository solutionDataTempRepository;

    @Autowired
    private QuestionInfoMathPixRepository questionInfoMathPixRepository;

    @Autowired
    private OptionsDataMathPixRepository optionsDataMathPixRepository;

    @Autowired
    private SolutionMathPixRepository solutionMathPixRepository;

    @Autowired
    private GetTempService getTempService;

    @Autowired
    private QuestionInfoRepository questionInfoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ApiResponse getQuestion(Integer questionId) {

        QuestionDataTemp questionData = questionDataTempRepository.getQuestionData(questionId.longValue());
        if (questionData == null) {
            throw new NotFoundException("Question not found");
        }
        Integer questionType = questionData.getQuestionType();
        List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(Long.valueOf(questionId));
        // Fetch options only if the type is NOT 3 or 6
        List<OptionTempDataDTO> optionDataList = (questionType == 3 || questionType == 6)
                ? new ArrayList<>()
                : getTempService.fetchOptionsByQuestionId(questionId.longValue());
        // Determine correct answer based on question type
        String correctAnswer;
        switch (questionType) {
            case 3 -> correctAnswer = getTempService.processNumericOptionsForGet(optionsDataTempList);
            case 6 -> correctAnswer = getTempService.processNumericAnswerForGet(optionsDataTempList);
            case 7 -> correctAnswer = getTempService.processType7(optionsDataTempList, questionType);
            default ->
                    correctAnswer = getTempService.processMultipleChoiceOptionsForGet(optionsDataTempList, questionType);
        }
        SolutionDataTemp solutionForQuestion = solutionDataTempRepository.getSolutionForQuestion(questionId.longValue());

        GetQuestionDto questionDto = GetQuestionDto.builder()
                .questionId(questionId.longValue())
                .questionLatex(questionData.getQuestionLatex())
                .questionImage(questionData.getQuestionImageUrl())
                .solutionImage(solutionForQuestion.getSolutionImageUrl())
                .questionImageProcessedUrl(
                        Optional.ofNullable(questionData.getQuestionImageProcessedUrl()).orElse(" ")
                )
                .answerKeyMatched(questionData.getIsAnsMatch())
                .options(optionDataList)
                .solutionLatex(solutionForQuestion.getSolutionLatex())
                .solutionImageProcessedUrl(
                        Optional.ofNullable(solutionForQuestion.getSolutionImageProcessedUrl()).orElse(" ")
                )
                .questionType(questionType)
                .questionSubType(questionData.getQuestionSubType())
                // .answerKey(correctAnswer)
                .build();

        return new ApiResponse(HttpStatus.OK, "Questions retrived successfully", questionDto);

    }

    private void validateRequiredFields(String questionLatex, String questionImageProcessedUrl, List<OptionTempDataDTO> options,
                                        String solutionLatex, String solutionImageProcessedUrl) {
        if ((questionLatex == null || questionLatex.trim().isEmpty()) &&
            (questionImageProcessedUrl == null || questionImageProcessedUrl.trim().isEmpty())) {
            throw new MissingDataException("Either question text or question image is required.");
        }

        boolean hasValidOptions = options.stream()
                .allMatch(option -> (option.getOptionLatex() != null && !option.getOptionLatex().trim().isEmpty()) ||
                                    (option.getOptionImageProcessedUrl() != null && !option.getOptionImageProcessedUrl().trim().isEmpty()));

        if (!hasValidOptions) {
            throw new MissingDataException("Each option must have either text or an image.");
        }

        if ((solutionLatex == null || solutionLatex.trim().isEmpty()) &&
            (solutionImageProcessedUrl == null || solutionImageProcessedUrl.trim().isEmpty())) {
            throw new MissingDataException("Either solution text or solution image is required.");
        }
//        if (answerKeyMatched == null) {
//            throw new MissingDataException("Answer key matched status is required.");
//        }

    }

    public ApiResponse updateTempQuestion(UpdateTempQuestionDTO updateTempQuestionDTO) {
        Long questionId = updateTempQuestionDTO.getQuestionId();
        String questionLatex = updateTempQuestionDTO.getQuestionLatex();
        String questionImageProcessedUrl = updateTempQuestionDTO.getQuestionImageProcessedUrl();
        String solutionLatex = updateTempQuestionDTO.getSolutionLatex();
        String solutionImageProcessedUrl = updateTempQuestionDTO.getSolutionImageProcessedUrl();
        List<OptionTempDataDTO> options = updateTempQuestionDTO.getOptions();
        // Boolean answerKeyMatched = updateTempQuestionDTO.getAnswerKeyMatched();
        validateRequiredFields(questionLatex, questionImageProcessedUrl, options, solutionLatex, solutionImageProcessedUrl);
        // Fetch existing question
        QuestionDataTemp existingQuestion = questionDataTempRepository.getQuestionForUpdate(questionId);

        if (existingQuestion == null) {
            throw new NotFoundException("Question ID " + questionId + " does not exist.");
        }

        Integer questionType = existingQuestion.getQuestionType(); // Fetch question type

        // Update question details
        existingQuestion.setQuestionLatex(questionLatex);
        existingQuestion.setQuestionImageProcessedUrl(questionImageProcessedUrl);
        // existingQuestion.setIsAnsMatch(answerKeyMatched);
        questionDataTempRepository.save(existingQuestion);

        // Update options **for all question types except 3 and 6**
        if (questionType != 3 && questionType != 6) {
            List<OptionsDataTemp> existingOptions = optionsDataTempRepository.getOptionsForQuestion(questionId);

            int minSize = Math.min(options.size(), existingOptions.size());
            for (int i = 0; i < minSize; i++) {
                OptionTempDataDTO inputOption = options.get(i);
                OptionsDataTemp existingOption = existingOptions.get(i);

                existingOption.setOptionLatex(inputOption.getOptionLatex());
                existingOption.setOptionImageProcessedUrl(inputOption.getOptionImageProcessedUrl());

                optionsDataTempRepository.save(existingOption);
            }
        }

        // Update solution details if it exists
        SolutionDataTemp existingSolutionOpt = solutionDataTempRepository.getSolutionForQuestion(questionId.longValue());
        if (existingSolutionOpt != null) {
            existingSolutionOpt.setSolutionLatex(solutionLatex);
            existingSolutionOpt.setSolutionImageProcessedUrl(solutionImageProcessedUrl);
            solutionDataTempRepository.save(existingSolutionOpt);
        }

        return new ApiResponse(HttpStatus.OK, "Temp question updated successfully", null);
    }

    @Transactional
    public ApiResponse saveAllQuestions() {
        try {
            List<QuestionDataTemp> questionTempData = questionDataTempRepository.getQuestionTempData();
            if (questionTempData == null || questionTempData.isEmpty()) {
                log.error("No questions found in temporary data.");
                throw new MissingDataException("No questions found in temporary data.");
            }

            // Cache existing questions to prevent duplicate inserts
            Map<Long, QuestionInfoMathPix> questionCache = questionInfoMathPixRepository
                    .findAllById(
                            questionTempData.stream()
                                    .map(QuestionDataTemp::getQuestionId)
                                    .map(Long::intValue)
                                    .collect(Collectors.toList())
                    )
                    .stream()
                    .collect(Collectors.toMap(q -> (long) q.getQuestionId(), q -> q));

            List<QuestionInfoMathPix> questionEntities = new ArrayList<>();
            List<OptionsDataMathPix> optionEntities = new ArrayList<>();
            List<SolutionMathPix> solutionEntities = new ArrayList<>();

            for (int i = 0; i < questionTempData.size(); i++) {
                QuestionDataTemp questionDataTemp = questionTempData.get(i);
                Long questionId = questionDataTemp.getQuestionId();
                Integer questionType = questionDataTemp.getQuestionType();

                // Fetch and update question complexity
                QuestionInfo byQuestionId = questionInfoRepository.findByQuestionId(questionId.intValue());
                if (byQuestionId != null) {
                    Integer newComplexity = questionDataTemp.getComplexity() != null ? questionDataTemp.getComplexity() : byQuestionId.getComplexity();

                    if (!newComplexity.equals(byQuestionId.getComplexity())) {
                        log.info("Before update: Question ID: {}, Complexity: {}", byQuestionId.getQuestion_id(), byQuestionId.getComplexity());

                        byQuestionId.setComplexity(newComplexity);
                        QuestionInfo save = questionInfoRepository.save(byQuestionId);
                        log.info("After update: Question ID: {}, Complexity: {}", save.getQuestion_id(), save.getComplexity());

                        entityManager.flush();
                        entityManager.detach(byQuestionId);
                    }
                }

                // Skip duplicate questions
                if (questionCache.containsKey(questionId)) {
                    log.info("Skipping duplicate question with ID: {}", questionId);
                    continue;
                }

                // Create and store new question
                QuestionInfoMathPix questionInfoMathPix = new QuestionInfoMathPix();
                questionInfoMathPix.setQuestionId(questionId.intValue());
                questionInfoMathPix.setQuestionImageUrl(questionDataTemp.getQuestionImageProcessedUrl());
                questionInfoMathPix.setQuestionText(questionDataTemp.getQuestionLatex());

                questionCache.put(questionId, questionInfoMathPix);
                questionEntities.add(questionInfoMathPix);

                // Process options based on question type
                if (questionType == 3 || questionType == 6) {
                    // ⚡ For type 3 and 6, add only **one option**
                    List<OptionsData> fetchedOptions = optionsDataRepository.fetchOptionsForSingleAns(questionId);
                    if (!fetchedOptions.isEmpty()) {
                        Integer optionId = fetchedOptions.get(0).getOptions_id().intValue(); // Take only the first option

                        // Check if option already exists in DB
                        boolean optionExists = optionsDataMathPixRepository.findByOptionIds(optionId).isPresent();
                        if (!optionExists) {
                            OptionsDataMathPix newOption = new OptionsDataMathPix();
                            newOption.setQuestionInfoMathPix(questionInfoMathPix);
                            newOption.setOptionId(optionId);
                            newOption.setOptionText(""); // Empty text
                            newOption.setOptionImageUrl(""); // Empty URL

                            optionEntities.add(newOption);
                            log.info("Added single option for question type {}: Question ID: {}, Option ID: {}", questionType, questionId, optionId);
                        } else {
                            log.info("Skipping duplicate option for question type {}: Question ID: {}, Option ID: {}", questionType, questionId, optionId);
                        }
                    }
                } else {
                    //  For all other question types, add **all options**
                    List<OptionsDataTemp> optionsDataTempList = optionsDataTempRepository.fetchOptionsTempByQuestionId(questionId);
                    for (OptionsDataTemp option : optionsDataTempList) {
                        if (optionsDataMathPixRepository.existsById(option.getOptionId().intValue())) {
                            log.info("Skipping duplicate option with ID: {}", option.getOptionId());
                            continue;
                        }

                        OptionsDataMathPix optionsDataMathPix = new OptionsDataMathPix();
                        optionsDataMathPix.setOptionId(option.getOptionId().intValue());
                        optionsDataMathPix.setOptionImageUrl(option.getOptionImageProcessedUrl());
                        optionsDataMathPix.setOptionText(option.getOptionLatex());
                        optionsDataMathPix.setQuestionInfoMathPix(questionInfoMathPix);

                        optionEntities.add(optionsDataMathPix);
                    }
                }

                // Process solution
                SolutionDataTemp solutionForQuestion = solutionDataTempRepository.getSolutionForQuestion(questionId);
                if (solutionForQuestion != null) {
                    boolean solutionExists = solutionMathPixRepository.findByQuestionInfoMathPix(questionInfoMathPix).isPresent();
                    if (!solutionExists) {
                        SolutionMathPix solutionMathPix = new SolutionMathPix();
                        solutionMathPix.setQuestionInfoMathPix(questionInfoMathPix);
                        solutionMathPix.setSolutionImageUrl(solutionForQuestion.getSolutionImageProcessedUrl());
                        solutionMathPix.setSolutionText(solutionForQuestion.getSolutionLatex());

                        solutionEntities.add(solutionMathPix);
                    } else {
                        log.info("Skipping duplicate solution for question ID: {}", questionId);
                    }
                }

                // Batch save every 20 records
                if (i % 20 == 0) {
                    batchSave(questionEntities, optionEntities, solutionEntities);
                }
            }

            // Final batch save
            batchSave(questionEntities, optionEntities, solutionEntities);

            // Delete temp data after processing
            solutionDataTempRepository.deleteAllInBatch();
            optionsDataTempRepository.deleteAllInBatch();
            questionDataTempRepository.deleteAllInBatch();

            return new ApiResponse(HttpStatus.OK, "Temp question data updated successfully, and temp data deleted", null);
        } catch (MissingDataException e) {
            log.error("Error: {}", e.getMessage());
            return new ApiResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error processing questions: {}", e.getMessage(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing questions", null);
        }
    }

    // Batch save method
    private void batchSave(List<QuestionInfoMathPix> questionEntities, List<OptionsDataMathPix> optionEntities, List<SolutionMathPix> solutionEntities) {
        questionEntities.forEach(entityManager::merge);
        optionEntities.forEach(entityManager::merge);
        solutionEntities.forEach(entityManager::merge);

        entityManager.flush();
        entityManager.clear();

        questionEntities.clear();
        optionEntities.clear();
        solutionEntities.clear();
    }


    public ApiResponse updateMatchedStatusForQuestion(Long questionId, Boolean matchedStatus) {
        log.info("Updating matched status for question ID {} to {}", questionId, matchedStatus);

        Optional<QuestionDataTemp> optionalQuestion = questionDataTempRepository.findById(questionId);

        if (optionalQuestion.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Question not found with ID: " + questionId, null);
        }

        QuestionDataTemp question = optionalQuestion.get();
        question.setIsAnsMatch(matchedStatus); // Assuming isAnsMatch is the correct field
        questionDataTempRepository.save(question);

        String statusMessage = matchedStatus ? "Marked as Matched" : "Marked as Not Matched";

//        return new ApiResponse(HttpStatus.OK, "Matched status updated successfully - " + statusMessage, Map.of(
//                "questionId", questionId,
//                "newStatus", matchedStatus
//        ));
        return new ApiResponse(HttpStatus.OK, "Matched status updated successfully - " + statusMessage,null);

    }
}