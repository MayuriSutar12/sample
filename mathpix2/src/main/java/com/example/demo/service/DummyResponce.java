package com.example.demo.service;

import com.example.demo.input_dto.UpdateTempQuestionDTO;
import com.example.demo.resp_dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DummyResponce {
    public ApiResponse checkGenAiConversionStatus() {
        return new ApiResponse(HttpStatus.OK, "Gen Ai conversion question count", 66.66);

    }

    public ApiResponse getDataFromTempList() {
        // Creating dummy options for question 1
        List<OptionTempDataDTO> options1 = Arrays.asList(
                new OptionTempDataDTO("x^2 + y^2 = 25", "https://dummy-url.com/option1.jpg"),
                new OptionTempDataDTO("x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}", "https://dummy-url.com/option2.jpg"),
                new OptionTempDataDTO("\\int x dx = \\frac{x^2}{2} + C", "https://dummy-url.com/option3.jpg"),
                new OptionTempDataDTO("E = mc^2", "https://dummy-url.com/option4.jpg")
        );

        // Creating dummy options for question 2
        List<OptionTempDataDTO> options2 = Arrays.asList(
                new OptionTempDataDTO("sin^2(x) + cos^2(x) = 1", "https://dummy-url.com/option5.jpg"),
                new OptionTempDataDTO("F = ma", "https://dummy-url.com/option6.jpg"),
                new OptionTempDataDTO("PV = nRT", "https://dummy-url.com/option7.jpg"),
                new OptionTempDataDTO("a^2 + b^2 = c^2", "https://dummy-url.com/option8.jpg")
        );

        // Creating dummy question 1
        QuestionTempDataDTO question1 = new QuestionTempDataDTO();
        question1.setQuestionId(1001L);
        question1.setQuestionLatex("x^2 + y^2 = 25");
        question1.setQuestionImageProcessedUrl("https://dummy-url.com/question1.jpg");
        question1.setOptions(options1);
        question1.setSolutionLatex("\\frac{x}{y} = 2");
        question1.setSolutionImageProcessedUrl("https://dummy-url.com/solution1.jpg");
        question1.setComplexity(3);
        question1.setAnswerKey("A,D");
        question1.setAnswerKeyMatched(true);

        // Creating dummy question 2
        QuestionTempDataDTO question2 = new QuestionTempDataDTO();
        question2.setQuestionId(1002L);
        question2.setQuestionLatex("sin^2(x) + cos^2(x) = 1");
        question2.setQuestionImageProcessedUrl("https://dummy-url.com/question2.jpg");
        question2.setOptions(options2);
        question2.setSolutionLatex("sin^2(x) + cos^2(x) = 1");
        question2.setSolutionImageProcessedUrl("https://dummy-url.com/solution2.jpg");
        question2.setComplexity(2);
        question2.setAnswerKey("A,C");
        question2.setAnswerKeyMatched(false);

        // Wrapping both questions in TempQuestionCount
        TempQuestionCount tempQuestionCount = TempQuestionCount.builder()
                .questionCount(2) // Now we have two questions
                .QuestionList(Arrays.asList(question1, question2)) // List containing both questions
                .build();

        // Returning ApiResponse
        return new ApiResponse(HttpStatus.OK, "Dummy data fetched successfully", tempQuestionCount);
    }


    public ApiResponse processTempList() {
        return new ApiResponse(HttpStatus.OK, "Data updated and saved successfully", null);

    }

    public ApiResponse getQuestion(Integer questionId) {


        List<OptionTempDataDTO> optionDataList = Arrays.asList(
                new OptionTempDataDTO("A", "https://dummy-url.com/optionA.jpg"),
                new OptionTempDataDTO("B", "https://dummy-url.com/optionB.jpg"),
                new OptionTempDataDTO("C", "https://dummy-url.com/optionC.jpg"),
                new OptionTempDataDTO("D", "https://dummy-url.com/optionD.jpg")
        );


        GetQuestionDto questionDto = GetQuestionDto.builder()
                .questionId(159210L)
                .questionLatex("x^2 + y^2 = 25")
                .questionImage("https://jeeni-question-images.s3.ap-south-1.amazonaws.com/question-images/JRBGeCbVw0Q=/gen.png")
                .solutionImage("https://jeeni-question-images.s3.ap-south-1.amazonaws.com/question-images/+kqJsbpdq/g=/soln.png")
                .questionImageProcessedUrl("https://dummy-url.com/question.jpg")
                .answerKeyMatched(true)
                .options(optionDataList)
                .solutionLatex("\\frac{x}{y} = 2")
                .solutionImageProcessedUrl("https://dummy-url.com/solution.jpg")
                .questionType(3)
                .questionSubType(1)
                .build();
        return new ApiResponse(HttpStatus.OK, "Questions retrived successfully", questionDto);
    }

    public ApiResponse updateTempQuestion(UpdateTempQuestionDTO updateTempQuestionDTO) {
        return new ApiResponse(HttpStatus.OK, "Questions updated successfully", null);


    }

    public ApiResponse saveAllQuestions() {
        return new ApiResponse(HttpStatus.OK, "Saved all questions successfully", null);

    }
}