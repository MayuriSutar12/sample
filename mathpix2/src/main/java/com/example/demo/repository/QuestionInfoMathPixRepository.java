package com.example.demo.repository;

import com.example.demo.model.QuestionInfoMathPix;
import com.example.demo.model.SolutionMathPix;
import com.example.demo.projectionInterface.QuestionSolutionInterface;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionInfoMathPixRepository extends JpaRepository<QuestionInfoMathPix, Integer> {

//    @Query(value ="SELECT  qinfo.questionText as QuestionText ,qinfo.questionImageUrl as QuestionImageUrl,optdata.optionText as OptionText,optdata.optionImageUrl as OptionImgUrl ,solmath.solutionText as SolutionText ,solmath.solutionImageUrl as SolutionImageUrl from question_info_math_pix as qinfo join options_data_math_pix as optdata on qinfo.questionId=optdata.question_id  join solution_math_pix as solmath on optdata.question_id=solmath.question_id where qinfo.questionId=:questionId", nativeQuery = true)
//    QuestionSolutionInterface findQuestionSolutionById(@Param("questionId") Integer questionId);

    @Query(value = "SELECT " +
            "qinfo.question_text AS questionText, " +
            "qinfo.question_image_url AS questionImageUrl, " +
            "qinfo.question_id AS questionId,"+
            "optdata.option_text AS optionText, " +
            "optdata.option_image_url AS optionImgUrl, " +
            "solmath.solution_text AS solutionText, " +
            "solmath.solution_image_url AS solutionImageUrl " +
            "FROM question_info_math_pix AS qinfo " +
            "JOIN options_data_math_pix AS optdata ON qinfo.question_id = optdata.question_id " +
            "JOIN solution_math_pix AS solmath ON qinfo.question_id = solmath.question_id " +
            "WHERE qinfo.question_id = :questionId",
            nativeQuery = true)
    List<QuestionSolutionInterface> findQuestionSolutionById(@Param("questionId") Integer questionId);



    @Query(value = "SELECT * FROM question_info_math_pix WHERE question_id = :questionId", nativeQuery = true)
    //Optional<QuestionInfoMathPix> findByQuestionId(Integer questionId);
    //@Query("SELECT q FROM QuestionInfoMathPix q WHERE q.questionId = :questionId")
    QuestionInfoMathPix findByQuestionId(@Param("questionId") Integer questionId);



}
