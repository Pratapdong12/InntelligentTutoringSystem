package com.assignment.ai.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.ai.model.Student;


public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsernameAndPassword(String username, String password);
}

