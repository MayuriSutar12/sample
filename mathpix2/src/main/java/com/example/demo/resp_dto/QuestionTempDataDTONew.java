package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionTempDataDTONew {
//    private Long questionId; // Unique identifier for the question
//    private String questionLatex; // Raw LaTeX for the question
//    private String questionImageProcessedUrl; // Processed image URL for the question (if any)
//    private List<OptionTempDataDTO> options; // List of options for the question
//    private String solutionLatex; // Raw LaTeX for the solution
//    private String solutionImageProcessedUrl; // Processed image URL for the solution (if any)
//    private Integer complexity; // Complexity level of the question
//    private String answerKey; // The correct answer for the question
//    private Boolean answerKeyMatched; // Whether the answer key matches
//    private List<ContentBlock> questionTextContent; // Text content for the question
//    private List<ContentBlock> questionLatexContent; // Inline Math content for the question
//    private List<List<ContentBlock>> optionTextContents; // Text content for each option
//    private List<List<ContentBlock>> optionLatexContents; // Inline Math content for each option
//    private List<ContentBlock> solutionTextContent; // Text content for the solution
//    private List<ContentBlock> solutionLatexContent; // Inline Math content for the solution
private Long questionId; // Unique identifier for the question
    private String questionLatex; // Raw LaTeX for the question
    private String questionImageProcessedUrl; // Processed image URL for the question (if any)
    private List<OptionTempDataDTO> options; // List of options for the question
    private String solutionLatex; // Raw LaTeX for the solution
    private String solutionImageProcessedUrl; // Processed image URL for the solution (if any)
    private Integer complexity; // Complexity level of the question
    private String answerKey; // The correct answer for the question
    private Boolean answerKeyMatched; // Whether the answer key matches

    // Here are the Text and LaTeX content fields for question, options, and solution
    private List<ContentBlock> questionTextContent; // Text content for the question
    private List<ContentBlock> questionLatexContent; // Inline Math content for the question
    private List<List<ContentBlock>> optionTextContents; // Text content for each option
    private List<List<ContentBlock>> optionLatexContents; // Inline Math content for each option
    private List<ContentBlock> solutionTextContent; // Text content for the solution
    private List<ContentBlock> solutionLatexContent; // Inline Math content for the solution
}
