package com.example.demo.config;

import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.SolutionDataTemp;

public class QuestionSolutionWrapper {
    private QuestionDataTemp question;
    private SolutionDataTemp solution;

    // Constructors
    public QuestionSolutionWrapper() {}

    public QuestionSolutionWrapper(QuestionDataTemp question, SolutionDataTemp solution) {
        this.question = question;
        this.solution = solution;
    }

    // Getters and Setters
    public QuestionDataTemp getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDataTemp question) {
        this.question = question;
    }

    public SolutionDataTemp getSolution() {
        return solution;
    }

    public void setSolution(SolutionDataTemp solution) {
        this.solution = solution;
    }
}
