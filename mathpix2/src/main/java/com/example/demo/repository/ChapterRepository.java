package com.example.demo.repository;



import com.example.demo.model.Chapter;
import com.example.demo.projectionInterface.QuestionDtoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    @Query(value = "SELECT qinfo.question_id AS questionId, " +
            "qinfo.question_type_id AS questionType, " +
            "qinfo.question_sub_type AS questionSubType, " +
            "qurls.generic_resolution_url AS questionImage " +
            "FROM chapter chp " +
            "INNER JOIN chapter_question_mapping chque ON chp.chapter__id = chque.chapterId " +
            "INNER JOIN questioninfo qinfo ON chque.questionId = qinfo.question_id " +
            "INNER JOIN question_s3_urls qurls ON qinfo.question_id = qurls.question_id " +
            "WHERE chp.chapter__id = :chapterId " +
            "AND qinfo.isDeleted = 0 " +
            "AND qinfo.approved = 1",
            nativeQuery = true)
    List<QuestionDtoInterface> findQuestionsWithImagesByChapterId(@Param("chapterId") Integer chapterId);


}
