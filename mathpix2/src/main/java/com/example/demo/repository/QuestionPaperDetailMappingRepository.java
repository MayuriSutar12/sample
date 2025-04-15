package com.example.demo.repository;

import com.example.demo.model.QuestionPaperDetailMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPaperDetailMappingRepository extends JpaRepository<QuestionPaperDetailMapping,Integer> {
}
