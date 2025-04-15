package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "options_data_temp")
public class OptionsDataTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "answer", length = 50)
    private String answer;

    @Column(name = "genAi_Answer", length = 50)
    private String genAiAnswer;

    @Column(name = "genAi_isAnswer")
    private Boolean genAiIsAnswer;

    @Column(name = "is_answer")
    private Boolean isAnswer; // Maps MySQL `bit(1)` to Java `Boolean`

    @Column(name = "optionId", nullable = false)
    private Long optionId;

    @Column(name = "option_image_processedUrl")
    private String optionImageProcessedUrl;

    @Column(name = "option_latex",  columnDefinition = "LONGTEXT")
    private String optionLatex;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionDataTemp question;

}

