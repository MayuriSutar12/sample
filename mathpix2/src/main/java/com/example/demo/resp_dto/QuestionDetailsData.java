package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDetailsData {
    private Long questionId;
    private String questionImageUrl;
    private int questionType;
    private int questionSubType;
    private String answerKey;
}
