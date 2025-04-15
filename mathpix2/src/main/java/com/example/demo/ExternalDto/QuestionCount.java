package com.example.demo.ExternalDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class QuestionCount {
//
//    @JsonProperty("questionCount")
//    private String questionCount;
//
//    @JsonProperty("questionVos")
//    private List<QuestionPaperVo> questionVos;
//
//
//}

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QuestionCount {

    @JsonProperty("questionCount")
    private Integer questionCount;

    @JsonProperty("questionVos")
    private List<QuestionVo> questionVos;

    // Default Constructor
    public QuestionCount() {
    }

    // Parameterized Constructor
    public QuestionCount(Integer questionCount, List<QuestionVo> questionVos) {
        this.questionCount = questionCount;
        this.questionVos = questionVos;
    }

    // Getters and Setters
    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public List<QuestionVo> getQuestionVos() {
        return questionVos;
    }

    public void setQuestionVos(List<QuestionVo> questionVos) {
        this.questionVos = questionVos;
    }

    // toString Method for Debugging
    @Override
    public String toString() {
        return "QuestionCount{" +
                "questionCount='" + questionCount + '\'' +
                ", questionVos=" + questionVos +
                '}';
    }
}
