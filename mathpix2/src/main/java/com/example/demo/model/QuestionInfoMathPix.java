package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question_info_math_pix", indexes = {
        @Index(name = "idx_question_id", columnList = "question_id")
})
@Immutable
public class QuestionInfoMathPix {
    @Id
    @Column(name = "question_id")
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Optional: Use if you want auto-increment
    private Integer questionId;


    @Column(name = "question_text")
    private String questionText;

    @Column(name = "question_image_url")
    private String questionImageUrl;

}
