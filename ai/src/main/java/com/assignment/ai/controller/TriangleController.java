package com.assignment.ai.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assignment.ai.service.OntologyService;

@Controller
public class TriangleController {

     @Autowired
     private OntologyService ontologyService;


    @GetMapping("/learnTriangle")
    public String showForm(Model model) {
        try {
            Map<String, String> triangleFormulaProperties = ontologyService.getTriangleFormulaDataProperties();
            model.addAttribute("triangleFormulaProperties", triangleFormulaProperties);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return "triangleForm"; // Return the name of your HTML template
    }
    @GetMapping("/addQuestion")
    public String showaddForm( ) {
       
        return "question";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(@RequestParam String questionName,@RequestParam String questionText, @RequestParam String correctAnswer,@RequestParam int level) {
    try {
        ontologyService.addQuestion(questionName,questionText, correctAnswer,level);
    } catch (Exception e) {
        e.printStackTrace(); // Handle exceptions appropriately
        // Optionally, you can add an error message to the model to display on the frontend
    }
    return "redirect:/addQuestion"; // Redirect to a suitable page after adding the question
}
    
    @GetMapping("/learnCircle")
    public String showCircleForm(Model model) {
        try {
            Map<String, String> circleFormulaProperties = ontologyService.getCircleFormulaDataProperties();
            model.addAttribute("circleFormulaProperties", circleFormulaProperties);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return "circleForm"; // Return the name of your HTML template
    }

    @PostMapping("/calculateArea")
    public String calculateArea(@RequestParam("base") double base, 
                                @RequestParam("height") double height, 
                                Model model) {
        // Calculate area of the triangle
        double area = 0.5 * base * height;
        
        // Add the area to the model to display it on the HTML page
        model.addAttribute("area", area);
        return "triangleForm";
    }
    @PostMapping("/calculateCircleArea")
    public String calculateCircleArea(@RequestParam("radius") double radius, Model model) {
        // Calculate the area of the circle using the formula Area = π * radius^2
        double area = Math.PI * Math.pow(radius, 2);
        
        // Add the area to the model to display it on the HTML page
        model.addAttribute("area", area);
        return "circleForm";
    }
    @GetMapping("/learnSquare")
    public String showSquareForm(Model model) {
        try {
            Map<String, String> squareFormulaProperties = ontologyService.getSquareFormulaDataProperties();
            model.addAttribute("squareFormulaProperties", squareFormulaProperties);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return "squareForm"; // Return the name of your HTML template
    }

    @PostMapping("/calculateSquareArea")
    public String calculateSquareArea(@RequestParam("side") double side, Model model) {
        // Calculate the area of the square using the formula Area = Side × Side
        double area = Math.pow(side, 2);
        
        // Add the area to the model to display it on the HTML page
        model.addAttribute("area", area);
        return "squareForm";
    }

    @GetMapping("/learnRectangle")
    public String showRectangleForm(Model model) {
        try {
            Map<String, String> rectangleFormulaProperties = ontologyService.getRectangleFormulaDataProperties();
            model.addAttribute("rectangleFormulaProperties", rectangleFormulaProperties);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return "rectangleForm"; // Return the name of your HTML template
    }

    @PostMapping("/calculateRectangleArea")
    public String calculateRectangleArea(@RequestParam("length") double length, 
                                         @RequestParam("width") double width, 
                                         Model model) {
        // Calculate the area of the rectangle using the formula Area = Length × Width
        double area = length * width;
        
        // Add the area to the model to display it on the HTML page
        model.addAttribute("area", area);
        return "rectangleForm";
    }
    @GetMapping("/")
    public String homeForm() {
        return "home";
    }
    @GetMapping("/about")
    public String aboutForm() {
        return "about";
    }
    @GetMapping("/contact")
    public String contactForm() {
        return "contact";
    }
}

