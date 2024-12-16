package com.assignment.ai.service;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.ai.model.Question;
import com.assignment.ai.model.Student;
import com.assignment.ai.model.TestResult;
import com.assignment.ai.repository.QuestionRepository;
import com.assignment.ai.repository.StudentRepository;
import com.assignment.ai.repository.TestResultRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private OntologyService ontologyService;

    public Student register(Student student) {
        return studentRepository.save(student);
    }

    public Student login(String username, String password) {
        return studentRepository.findByUsernameAndPassword(username, password);
    }

    public List<Question> getQuestionsForLevel(int level) {
        return questionRepository.findByLevel(level);
    }

    public void saveTestResult(TestResult result) {
        testResultRepository.save(result);
    }

    public boolean canProceedToNextLevel(Student student, int level) {
        List<TestResult> results = testResultRepository.findByStudentAndLevel(student, level);
        return results.stream().anyMatch(r -> r.getScore() >= 7); // Assuming 7 is the passing score
    }

    
}

