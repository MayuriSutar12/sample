package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSolutionUrls {

    private String questionImageUrl;
    private String solutionImageUrl;
    private int questionType;
    private int subQuestionType;
}
