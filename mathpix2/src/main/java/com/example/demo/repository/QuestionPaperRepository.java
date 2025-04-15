package com.example.demo.repository;

import com.example.demo.model.QuestionPaper;
import com.example.demo.projectionInterface.QuestionDtoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionPaperRepository extends JpaRepository<QuestionPaper,Integer> {

    @Query(value = "SELECT qinfo.question_id AS questionId, " +
            "qinfo.question_type_id AS questionType, " +
            "qinfo.question_sub_type AS QuestionSubType, " +
            "qurls.generic_resolution_url AS questionImage " +
            "FROM question_paper AS qp " +
            "INNER JOIN question_paper_details AS qpd ON qpd.question_paper_id = qp.id " +
            "INNER JOIN question_paper_detail_mapping AS qpdm ON qpdm.question_paper_detail_id = qpd.id " +
            "INNER JOIN questioninfo AS qinfo ON qpdm.question_id = qinfo.question_id " +
            "INNER JOIN question_s3_urls AS qurls ON qinfo.question_id = qurls.question_id " +
            "WHERE qpdm.question_paper_detail_id = :questionPaperDetailId " +
            "AND qinfo.approved = 1 " +
            "AND qinfo.isDeleted = 0 " +
            "GROUP BY qpdm.question_id " +
            "ORDER BY qpdm.question_id",
            nativeQuery = true)
    List<QuestionDtoInterface> findQuestionsByQuestionPaperDetailId(@Param("questionPaperDetailId") Integer questionPaperDetailId);
}