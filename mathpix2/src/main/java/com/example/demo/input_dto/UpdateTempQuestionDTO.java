package com.example.demo.input_dto;

import com.example.demo.resp_dto.OptionTempDataDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTempQuestionDTO {
    @NotNull(message = "Question ID cannot be null")
    private Long questionId;
   // private Integer questionType;
    private String questionLatex;
    private String questionImageProcessedUrl;
    private List<OptionTempDataDTO> options;
    private String solutionLatex;
    private String solutionImageProcessedUrl;
  //  private Boolean answerKeyMatched;
}
