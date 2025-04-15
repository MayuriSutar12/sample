package com.example.demo.repository;

import com.example.demo.model.QuestionInfoAI;
import com.example.demo.model.SolutionDataTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionInfoAiRepository extends JpaRepository<QuestionInfoAI,Integer> {

    @Query(value = "select * from m_questioninfo_ai where question_id=:questionId",nativeQuery = true)
    QuestionInfoAI getForQuestion(@Param("questionId") Long questionId);

    @Query("SELECT COUNT(q) > 0 FROM QuestionInfoAI q WHERE q.questionId = :questionId")
    boolean existsByQuestionId(@Param("questionId") Long questionId);
}
