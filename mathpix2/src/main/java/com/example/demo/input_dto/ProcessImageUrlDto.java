package com.example.demo.input_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessImageUrlDto {
    private Long questionId;
    private String questionUrl;
    private String solutionUrl;
    private Integer questionType;
}
