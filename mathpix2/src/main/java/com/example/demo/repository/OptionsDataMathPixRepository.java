package com.example.demo.repository;

import com.example.demo.model.OptionsDataMathPix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionsDataMathPixRepository extends JpaRepository<OptionsDataMathPix, Integer> {


    @Query(value = "SELECT * FROM options_data_math_pix o WHERE o.question_id = :questionId ", nativeQuery = true)
    List<OptionsDataMathPix> findOptionsByQuestionId(@Param("questionId") Integer questionId);

    //Optional<OptionsDataMathPix> findByQuestionIdAndOptionNumber(Integer questionId, Integer optionId);
    Optional<OptionsDataMathPix> findByOptionId(Integer optionId);


    @Query(value = "Select * from options_data_math_pix o WHERE o.option_id = :optionId",nativeQuery = true)
    Optional<OptionsDataMathPix> findByOptionIds(Integer optionId);
}
