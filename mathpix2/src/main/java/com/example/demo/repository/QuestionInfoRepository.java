package com.example.demo.repository;

import com.example.demo.model.QuestionInfo;
import com.example.demo.projectionInterface.QuestionDtoInterface;
import com.example.demo.projectionInterface.QuestionInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionInfoRepository extends JpaRepository<QuestionInfo,Long> {

    @Query(value="SELECT q.* FROM questioninfo as q inner join question_s3_urls as qs on q.question_id=qs.question_id where q.question_id=:questionId and approved=true and isDeleted=false",nativeQuery = true)
    Optional<QuestionInfo> presentedQuestion(Integer questionId);

    @Query(value="select info.* from questioninfo as info inner join optionsdata as opt on info.question_id=opt.question_id where info.question_id=:questionId",nativeQuery = true)
    Optional<QuestionInfo> QuestionId(Integer questionId);

    @Query(value = """
        SELECT 
            qinfo.question_type_id AS questionType, 
            qinfo.question_sub_type AS questionSubType, 
            qurls.generic_resolution_url AS genericUrl 
        FROM questioninfo AS qinfo 
        INNER JOIN question_s3_urls AS qurls 
        ON qinfo.question_id = qurls.question_id 
        WHERE qinfo.question_id = :questionId
        """, nativeQuery = true)
    QuestionInfoProjection getQuestionInfoById(@Param("questionId") Long questionId);

    @Query(value = "SELECT * FROM questioninfo WHERE question_id = :questionId", nativeQuery = true)
    QuestionInfo findByQuestionId(@Param("questionId") Integer questionId);
}
