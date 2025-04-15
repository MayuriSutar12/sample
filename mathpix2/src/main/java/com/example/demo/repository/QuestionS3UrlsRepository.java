package com.example.demo.repository;

import com.example.demo.model.QuestionS3Urls;
import com.example.demo.projectionInterface.QuestionDetailsInterface;
import com.example.demo.projectionInterface.QuestionUrlInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionS3UrlsRepository extends JpaRepository<QuestionS3Urls,Integer> {

    @Query(value = "SELECT * FROM question_s3_urls WHERE question_id=:questionId", nativeQuery = true)
    QuestionS3Urls getByQuestionId(int questionId);

    @Query(value = "SELECT qs3.generic_resolution_url as QuestionUrl ,qs3.solution_url as SolutionUrl,qinfo.question_type_id as QuestionType,qinfo.question_sub_type as QuestionSubType  FROM question_s3_urls as qs3 inner join questioninfo as qinfo on qs3.question_id=qinfo.question_id WHERE qs3.question_id = :questionId", nativeQuery = true)
    QuestionDetailsInterface getQuestionDetailsById(@Param("questionId") int questionId);
    @Query(value = "SELECT qs3.generic_resolution_url AS QuestionUrl, " +
            "qinfo.question_type_id AS QuestionType, " +
            "qinfo.question_sub_type AS QuestionSubType, " +
            "qinfo.question_id AS questionId " +
            "FROM question_s3_urls qs3 " +
            "INNER JOIN questioninfo qinfo ON qs3.question_id = qinfo.question_id " +
            "WHERE qinfo.isDeleted = 0 AND qinfo.approved = 1 AND qinfo.question_sub_type = 2 AND qinfo.question_type_id = 7 " +  // <-- Added space before ORDER BY
            "ORDER BY qinfo.question_id ASC " +
            "LIMIT 50 OFFSET :offset",
            nativeQuery = true)
    List<QuestionUrlInterface> getQuestionDetailsPaginated(@Param("offset") int offset);


}
