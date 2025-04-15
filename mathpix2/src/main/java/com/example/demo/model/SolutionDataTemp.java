package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solution_data_temp")
public class SolutionDataTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_id")
    private Integer solutionId;

    @Column(name = "solution_image_processedUrl", length = 255)
    private String solutionImageProcessedUrl;

    @Column(name = "solution_latex", columnDefinition = "LONGTEXT")
    private String solutionLatex;

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionDataTemp question;

    @Column(name = "solution_image_url", length = 1000, nullable = false)
    private String solutionImageUrl;

}
