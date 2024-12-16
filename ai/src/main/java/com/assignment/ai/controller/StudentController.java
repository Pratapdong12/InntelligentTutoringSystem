package com.assignment.ai.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assignment.ai.model.Question;
import com.assignment.ai.model.Student;
import com.assignment.ai.model.TestResult;
import com.assignment.ai.service.OntologyService;
import com.assignment.ai.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private OntologyService ontologyService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    // @PostMapping("/register")
    // public String register(@ModelAttribute Student student) {
    //     studentService.register(student);
    //     return "redirect:/login";
    // }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Student student = studentService.login(username, password);
        if (student != null) {
            session.setAttribute("student", student);
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) return "redirect:/login";
        model.addAttribute("student", student);
        return "dashboard";
    }

   

    @GetMapping("/test")
    public String test(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) return "redirect:/login";
        System.out.println(student.getCurrentLevel());
        if (student.getCurrentLevel() == 4) {
            model.addAttribute("completedTest", true);  // Add a flag to the model
        } else {
            model.addAttribute("completedTest", false);
        }
        List<Question> questions = studentService.getQuestionsForLevel(student.getCurrentLevel());
        model.addAttribute("questions", questions);
        // Check if the student's level is 3 and set a flag for the completion message
    
        return "test";
    }

//     @PostMapping("/test")
// public String submitTest(@RequestParam Map<String, String> answers, HttpSession session, Model model) {
//     Student student = (Student) session.getAttribute("student");
//     if (student == null) return "redirect:/login";

//     // Get questions based on the student's current level
//     List<Question> questions = studentService.getQuestionsForLevel(student.getCurrentLevel());

//     // Evaluate the answers
//     int score = evaluateTest(answers, questions);

//     // Save the result in the database
//     TestResult result = TestResult.builder()
//             .student(student)
//             .level(student.getCurrentLevel())
//             .score(score)
//             .build();
//     studentService.saveTestResult(result);

//     // Provide feedback and update the level if needed
//     String feedback = generateFeedback(score);
//     model.addAttribute("score", score);
//     model.addAttribute("feedback", feedback);

//     if (score >= 7) {
//         if (studentService.canProceedToNextLevel(student, student.getCurrentLevel())) {
//             student.setCurrentLevel(student.getCurrentLevel() + 1);  // Update to next level
//             studentService.register(student);  // Save updated student info
//             model.addAttribute("nextLevel", true);
//         }
//     }

//     return "result"; // Redirect to the result page
// }


private int evaluateTest(Map<String, String> answers, List<Question> questions) {
    int score = 0;
    
    for (int i = 0; i < questions.size(); i++) {
        String correctAnswer = questions.get(i).getAnswer();
        String userAnswer = answers.get("answers[" + i + "]"); // Get user answer from Map using index
        System.out.println(correctAnswer);
        System.out.println("----------");
        System.out.println(userAnswer);    
        if (userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
            score++;
        }
    }
    
    return score;
}

private String generateFeedback(int score) {
    if (score >= 7) {
        return "Congratulations! You passed the test.";
    } else {
        return "Try again. You can do better!";
    }
}

@PostMapping("/resetLevel")
public String resetLevel(HttpSession session, Model model) {
    int score = 0;
    Student student = (Student) session.getAttribute("student");
    if (student == null) return "redirect:/login";
    
    // Reset the student's level to 1 (Beginner)
    student.setCurrentLevel(1);
    studentService.register(student);  // Save updated student info

    // Add feedback message
    model.addAttribute("levelReset", true);
   
    try {
        ontologyService.resetStudentScoreAndLevel(student.getUsername(), score, student.getCurrentLevel());
       
    } catch (Exception e) {
        e.printStackTrace(); // Handle exceptions appropriately
    }

    return "redirect:/dashboard";  // Redirect to dashboard or any other appropriate page
}
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();  // Invalidate the session to log out the user
    return "redirect:/login";  // Redirect to login page after logging out
}
@PostMapping("/register")
public String register(@ModelAttribute Student student) {
    studentService.register(student);
    try {
        ontologyService.addStudent(student.getUsername(), student.getFirstName(), student.getLastName());
    } catch (Exception e) {
        e.printStackTrace(); // Handle exceptions appropriately
    }
    return "redirect:/login";
}

@PostMapping("/test")
public String submitTest(@RequestParam Map<String, String> answers, HttpSession session, Model model) {
    Student student = (Student) session.getAttribute("student");
    if (student == null) return "redirect:/login";

    // Get questions based on the student's current level
    List<Question> questions = studentService.getQuestionsForLevel(student.getCurrentLevel());

    // Evaluate the answers
    int score = evaluateTest(answers, questions);

    // Save the result in the database
    TestResult result = TestResult.builder()

            .student(student)
            .level(student.getCurrentLevel())
            .score(score)
            .build();
    studentService.saveTestResult(result);

    // // Add the test result to the ontology
    // try {
    //     ontologyService.addTestResult(student.getFirstName(), score);
    // } catch (Exception e) {
    //     e.printStackTrace(); // Handle exceptions appropriately
    // }
    try {
        ontologyService.updateStudentScoreAndLevel(student.getUsername(), score, student.getCurrentLevel());
       
    } catch (Exception e) {
        e.printStackTrace(); // Handle exceptions appropriately
    }

    // Provide feedback and update the level if needed
    String feedback = generateFeedback(score);
    model.addAttribute("score", score);
    model.addAttribute("feedback", feedback);

    if (score >= 7) {
        if (studentService.canProceedToNextLevel(student, student.getCurrentLevel())) {
            student.setCurrentLevel(student.getCurrentLevel() + 1);  // Update to next level
            studentService.register(student);  // Save updated student info
            model.addAttribute("nextLevel", true);
        }
        
    }

    return "result"; // Redirect to the result page
    
}
}
    

   
