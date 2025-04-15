package com.example.demo.ExternalDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestData {

    private int id;
    private String name;
    private int noOfQuestions;
    private int orgId;
    private int subjectId;
    private int testType;
    private int courseId;
    private boolean isNewQuestions;
    private List<QuestionData> questionData;
    private boolean isAutoGenerate;
    private String createdBy;
    private String subjectName;
    private String createdOn;
    private String testNames;
    private int noOfTestAssociated;
    private int syncStatus;
}
