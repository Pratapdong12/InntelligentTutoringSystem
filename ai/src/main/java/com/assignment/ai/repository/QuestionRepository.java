package com.assignment.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.ai.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByLevel(int level);
    
}


