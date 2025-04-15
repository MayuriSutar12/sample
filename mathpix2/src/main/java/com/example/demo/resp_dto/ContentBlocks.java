package com.example.demo.resp_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentBlocks {
    private String type; // Type of content: "textContent", "InlineMath", or "BlockMath"
    private String value; // Content value (either LaTeX or plain text)
}
