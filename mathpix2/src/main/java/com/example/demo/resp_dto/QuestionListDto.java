package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListDto {
    private Integer questionId;
    private String questionUrl;
    private String questionType;
    private String solutionUrl;
    private String answerKey;
}
