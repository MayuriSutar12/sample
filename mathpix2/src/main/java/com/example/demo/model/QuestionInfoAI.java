package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_questioninfo_ai")
public class QuestionInfoAI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdOn", nullable = false) // Explicitly map to the correct column name
    private LocalDateTime createdOn;

    @Column(name = "GenAi_topics")
    private String genAiTopics;

    @Column(name = "status_ai")
    private Boolean statusAi;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "complexity")
    private Integer complexity;

    @Column(name = "ai_answer")
    private String aiAnswer;
}

