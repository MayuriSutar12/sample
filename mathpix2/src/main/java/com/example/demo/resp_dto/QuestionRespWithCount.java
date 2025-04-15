package com.example.demo.resp_dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionRespWithCount {
    private Integer questionCount;
    private List<QuestionListDto> QuestionList;
}
