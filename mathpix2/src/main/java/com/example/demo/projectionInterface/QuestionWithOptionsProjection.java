package com.example.demo.projectionInterface;

public interface QuestionWithOptionsProjection {
    Long getQuestionId();
    Integer getQuestionType();
    Integer getQuestionSubType();
    Long getOptionId();
    Boolean getIsAnswer();
    String getAnswer();

    String getSolutionImageUrl();

    String getQuestionImageUrl();
}

