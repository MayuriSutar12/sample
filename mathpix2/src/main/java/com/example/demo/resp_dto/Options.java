package com.example.demo.resp_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Options {
    //  private Integer optionId;
    @NotBlank(message = "option cannot be blank")
    private String optionText;
}
