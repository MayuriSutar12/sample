package com.example.demo.model;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_paper_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class QuestionPaperDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "no_of_questions")
    private Integer noOfQuestions;

    @Column(name = "org_id")
    private Integer orgId;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "chapter_id")
    private Integer chapterId;

    @ManyToOne
    @JoinColumn(name = "question_paper_id", referencedColumnName = "id")
    private QuestionPaper questionPaper;

    @Column(name = "sync_status")
    private Integer syncStatus;
}

