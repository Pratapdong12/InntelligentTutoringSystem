package com.assignment.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.ai.model.Student;
import com.assignment.ai.model.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByStudentAndLevel(Student student, int level);
}
