package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question_s3_urls")
@Immutable
public class QuestionS3Urls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "mdpi_url", length = 255)
    private String mdpiUrl;

    @Column(name = "generic_resolution_url", length = 255)
    private String genericResolutionUrl;

    @Column(name = "ldpi_url", length = 255)
    private String ldpiUrl;

    @Column(name = "hdpi_url", length = 255)
    private String hdpiUrl;

    @Column(name = "baseimage_url", length = 255)
    private String baseimageUrl;

    @Column(name = "a4_url", length = 255)
    private String a4Url;

    @Column(name = "solution_url", length = 255)
    private String solutionUrl;
}
