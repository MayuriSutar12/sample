package com.example.demo.model;



import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class QuestionPaperDetailMappingId implements Serializable {

    @Column(name = "question_paper_detail_id")
    private Integer questionPaperDetailId;

    @Column(name = "question_id")
    private Integer questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionPaperDetailMappingId that = (QuestionPaperDetailMappingId) o;
        return Objects.equals(questionPaperDetailId, that.questionPaperDetailId) &&
                Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionPaperDetailId, questionId);
    }
}
