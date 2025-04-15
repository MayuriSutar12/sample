package com.example.demo.repository;

import com.example.demo.model.QuestionInfoMathPix;
import com.example.demo.model.SolutionMathPix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionMathPixRepository extends JpaRepository<SolutionMathPix, Integer> {


    @Query(value = "SELECT * FROM solution_math_pix WHERE question_id = :questionId", nativeQuery = true)
    Optional<SolutionMathPix> findByQuestionId(@Param("questionId") Integer questionId);

    //boolean existsByQuestionId(Integer questionId);
    @Query("SELECT COUNT(s) > 0 FROM SolutionMathPix s WHERE s.questionInfoMathPix.id = :questionId")
    boolean existsByQuestionId(@Param("questionId") Integer questionId);

    //    @Query("SELECT s FROM SolutionMathPix s WHERE s.questionInfoMathPix.question_id = :questionId")
//    Optional<SolutionMathPix> findByQuestionInfoMathPixId(@Param("questionId") Integer questionId);
    Optional<SolutionMathPix> findByQuestionInfoMathPix(QuestionInfoMathPix questionInfoMathPix);

}
