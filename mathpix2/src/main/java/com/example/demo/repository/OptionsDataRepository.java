package com.example.demo.repository;

import com.example.demo.model.OptionsData;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.QuestionInfo;
import com.example.demo.projectionInterface.QuestionWithOptionsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionsDataRepository extends JpaRepository<OptionsData, Long> {

    /*@Query(value = "SELECT * FROM optionsdata WHERE question_id IN(:ids) ORDER BY question_id ASC", nativeQuery = true)
    List<OptionsData> fetchOptionsByIds(@Param("ids") List<Long> ids);*/

    @Query(value = "SELECT * FROM optionsdata WHERE question_id = :id", nativeQuery = true)
    List<OptionsData> fetchOptionsById(@Param("id")Long questionId);

    @Query("select count(o) from OptionsData o where o.question_id = ?1 and o.is_answer = true")
    long countByQuestionIdAndIsAnswerTrue(QuestionInfo questionId);

    @Query(value = "SELECT * FROM optionsdata WHERE question_id = :id and is_answer=1", nativeQuery = true)
    List<OptionsData> fetchOptionsForSingleAns(@Param("id")Long questionId);

    @Query(value = "SELECT * FROM optionsdata WHERE question_id = :id", nativeQuery = true)
    Optional<OptionsData> fetchOptionsForMultipleAns(@Param("id")Long questionId);
    @Query(value = "SELECT optdata.* FROM optionsdata AS optdata " +
            "INNER JOIN questioninfo AS qinfo ON optdata.question_id = qinfo.question_id " +
            "WHERE qinfo.question_id = :id",
            nativeQuery = true)
    List<OptionsData> fetchOptionsByQuestionId(@Param("id") Long questionId);


    @Query(value = "SELECT q.question_id AS questionId, q.question_type_id AS questionType, q.question_sub_type AS questionSubType, " +
            "o.options_id AS optionId, o.is_answer AS isAnswer, o.answer AS answer,qurls.solution_url as solutionImageUrl ,qurls.generic_resolution_url as questionImageUrl " +
            "FROM optionsdata o " +
            "INNER JOIN questioninfo q ON o.question_id = q.question_id " +
            "INNER JOIN question_s3_urls as qurls on q.question_id=qurls.question_id "+
            "WHERE q.question_id = :questionId",nativeQuery = true)
    List<QuestionWithOptionsProjection> findOptionsByQuestionId(@Param("questionId") Long questionId);



}
