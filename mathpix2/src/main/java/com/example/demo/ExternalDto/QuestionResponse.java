package com.example.demo.ExternalDto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private List<QuestionVo> questions;
}

