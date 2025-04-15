package com.example.demo.repository;

import com.example.demo.model.QuestionDataTemp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface QuestionDataTempRepository extends JpaRepository<QuestionDataTemp, Long> {

//@Query(value = "SELECT COUNT(*) FROM question_data_temp WHERE genAi_conversion_completed = 1", nativeQuery = true)
//long countCompletedGenAiConversions();

    @Query(value = """
            SELECT 
                (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM question_data_temp)) 
            FROM question_data_temp
            WHERE genAi_conversion_completed = TRUE
            """, nativeQuery = true)
    Double countCompletedGenAiConversions();

    @Query(value = "SELECT * FROM question_data_temp", nativeQuery = true)
    List<QuestionDataTemp> getQuestionTempData();

    @Query(value = "SELECT * FROM question_data_temp WHERE question_id = :questionId LIMIT 1", nativeQuery = true)
    QuestionDataTemp getQuestionData(@Param("questionId") Long questionId);

    @Query(value = "SELECT * FROM question_data_temp WHERE question_id = :questionId", nativeQuery = true)
    QuestionDataTemp getQuestionForUpdate(@Param("questionId") Long questionId);


    @Query(value = "SELECT * FROM question_data_temp WHERE genAi_conversion_completed = FALSE ORDER BY question_id ASC LIMIT :batchSize",
            nativeQuery = true)
    List<QuestionDataTemp> fetchUnprocessedQuestions(@Param("batchSize") int batchSize);



    @Query("SELECT COUNT(q) FROM QuestionDataTemp q " +
            "WHERE q.genAiConversionCompleted = true " +
            "AND q.updatedOn >= :startOfDay " +
            "AND q.updatedOn < :endOfDay")
    int getProcessedCountByDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);


    @Query(value = "SELECT * FROM question_data_temp q WHERE q.genAi_conversion_completed = false LIMIT :limit",
            nativeQuery = true)
    List<QuestionDataTemp> getLimitedQuestions(@Param("limit") int limit);



    @Query(value = "SELECT COUNT(*) FROM question_data_temp qtemp " +
                   "JOIN options_data_temp otemp ON qtemp.question_id = otemp.question_id " +
                   "JOIN solution_data_temp stemp ON otemp.question_id = stemp.question_id " +
                   "WHERE qtemp.question_id = :questionId",
            nativeQuery = true)
    int countByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT q FROM QuestionDataTemp q WHERE q.questionId IN :ids")
    List<QuestionDataTemp> findByQuestionIdIn(@Param("ids") List<Long> ids);




}
