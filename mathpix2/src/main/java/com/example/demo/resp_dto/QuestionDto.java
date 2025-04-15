package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long questionId;
    private String questionLatex;
    private String questionImageProcessedUrl;
    private List<OptionTempDataDTO> options;
    private String solutionLatex;
    private String solutionImageProcessedUrl;
    //private Integer complexity;
   // private String answerKey;
    private Boolean answerKeyMatched;
    private Integer questionType;
    private Integer questionSubType;
}
