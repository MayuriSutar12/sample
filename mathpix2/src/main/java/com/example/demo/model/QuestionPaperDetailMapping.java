package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "question_paper_detail_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class QuestionPaperDetailMapping {

    @EmbeddedId
    private QuestionPaperDetailMappingId id;

    @ManyToOne
    @MapsId("questionPaperDetailId")
    @JoinColumn(name = "question_paper_detail_id", referencedColumnName = "id", nullable = false)
    private QuestionPaperDetails questionPaperDetail;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false)
    private QuestionInfo questionInfo;

    @Column(name = "sync_status")
    private Integer syncStatus;
}
