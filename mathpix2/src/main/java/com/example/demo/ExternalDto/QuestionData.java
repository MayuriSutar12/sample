package com.example.demo.ExternalDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionData {

    private int chapterId;
    private int questionsCount;
    private String chapterName;
}
