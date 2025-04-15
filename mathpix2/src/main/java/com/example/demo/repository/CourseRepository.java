package com.example.demo.repository;


import com.example.demo.model.Course;
import com.example.demo.projectionInterface.SubjectDtoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

       @Query(value = "SELECT sub.subject_id AS subjectId, sub.subject_name AS subjectName " +
            "FROM course AS c " +
            "INNER JOIN course_subject_mapping AS csub ON c.course_id = csub.courseId " +
            "INNER JOIN subject AS sub ON csub.subjectId = sub.subject_id " +
            "WHERE c.course_id = :courseId", nativeQuery = true)
    List<SubjectDtoInterface> findSubjectsByCourseId(@Param("courseId") Integer courseId);

}
