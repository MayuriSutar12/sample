package com.example.demo.repository;

import com.example.demo.model.OptionsData;
import com.example.demo.model.OptionsDataTemp;
import com.example.demo.model.QuestionDataTemp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OptionsDataTempRepository extends JpaRepository<OptionsDataTemp,Integer> {


    @Query(value = "SELECT optdata.* FROM options_data_temp AS optdata " +
            "INNER JOIN question_data_temp AS qdata ON optdata.question_id = qdata.question_id " +
            " WHERE qdata.question_id = :id",
            nativeQuery = true)
    List<OptionsDataTemp> fetchOptionsTempByQuestionId(@Param("id") Long questionId);

    boolean existsByQuestion(QuestionDataTemp question);


    @Query(value="select * from options_data_temp where question_id=:questionId",nativeQuery = true)
    List<OptionsDataTemp> getOptionsForQuestion(@Param("questionId") Long questionId);


    @Query(value = "SELECT * FROM options_data_temp WHERE question_id = :id and is_answer=1", nativeQuery = true)
    List<OptionsDataTemp> fetchOptionsForSingleAns(@Param("id")Long questionId);


    @Query(value = "SELECT * FROM options_data_temp WHERE question_id = :id and is_answer=1", nativeQuery = true)
    List<OptionsDataTemp> fetchOptionsForSingle(@Param("id")Long questionId);
}
