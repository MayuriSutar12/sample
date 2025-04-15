package com.example.demo.repository;


import com.example.demo.model.Subject;
import com.example.demo.projectionInterface.ChapterDtoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {

    @Query(value = "SELECT * FROM subject sub " +
            "INNER JOIN subject_chapter_mapping scmap ON sub.subject_id = scmap.subjectId " +
            "INNER JOIN chapter ch ON scmap.chapterId = ch.chapter__id " +
            "WHERE sub.subject_id = :subjectId",
            nativeQuery = true)
    List<ChapterDtoInterface> findChaptersBySubjectId(@Param("subjectId") Integer subjectId);

}
