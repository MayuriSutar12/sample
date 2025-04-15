package com.example.demo.repository;

import com.example.demo.model.OptionsDataTemp;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.SolutionDataTemp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SolutionDataTempRepository extends JpaRepository<SolutionDataTemp,Integer> {

    @Query(value = "select soldata.* from question_data_temp as qdata inner join solution_data_temp as soldata on qdata.question_id=soldata.question_id where qdata.question_id=:questionId", nativeQuery = true)
    SolutionDataTemp fetchSolutionForQuestionId(@Param("questionId") Long questionId);

    boolean existsByQuestion(QuestionDataTemp question);

    @Query(value = "select * from solution_data_temp where question_id=:questionId", nativeQuery = true)
    SolutionDataTemp getSolutionForQuestion(@Param("questionId") Long questionId);

    @Query(value = "select soldata.* from question_data_temp as qdata inner join solution_data_temp as soldata on qdata.question_id=soldata.question_id where qdata.question_id=:questionId and qdata.genAi_conversion_completed=false", nativeQuery = true)
    List<SolutionDataTemp> fetchSolutionForQuestionIdStatus(@Param("questionId") Long questionId);
}
