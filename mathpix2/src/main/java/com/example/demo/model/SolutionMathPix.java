package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solution_math_pix")
@Immutable
public class SolutionMathPix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "solution_text")
    private String solutionText;

    @Column(name = "solution_image_url")
    private String solutionImageUrl;

    // One-to-one relationship with QuestionInfoMathPix
    @OneToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    private QuestionInfoMathPix questionInfoMathPix;

}
