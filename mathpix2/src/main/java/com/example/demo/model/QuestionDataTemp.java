package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question_data_temp", indexes = {
        @Index(name = "idx_question_id", columnList = "question_id"),
        @Index(name = "idx_genAi_conversion_completed", columnList = "genAi_conversion_completed"),
        @Index(name = "idx_updated_on", columnList = "updatedOn")  // Add index for faster date filtering
})
public class QuestionDataTemp {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "complexity")
    private Integer complexity;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "IsAnsMatch")
    private Boolean isAnsMatch = false;

    @Column(name = "question_image_processedUrl")
    private String questionImageProcessedUrl;

    @Column(name = "question_imageUrl", nullable = false)
    private String questionImageUrl;

    @Column(name = "question_latex", columnDefinition = "LONGTEXT")
    private String questionLatex;

    @Column(name = "Question_sub_type", nullable = false)
    private Integer questionSubType;

    @Column(name = "Question_type", nullable = false)
    private Integer questionType;

    @Column(name = "updatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "genAi_conversion_completed", nullable = false)
    private Boolean genAiConversionCompleted;

}
