package com.assignment.ai.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.assignment.ai.model.Question;


@Service
public class OntologyService {
    private OWLOntology ontology;

     public OntologyService() throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        Resource resource = new ClassPathResource("areaLearning.owl");
        ontology = manager.loadOntologyFromOntologyDocument(resource.getInputStream());
    }

    public void addStudent(String username, String firstName, String lastName) throws OWLOntologyStorageException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
    
        IRI studentIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#" + username);
        OWLNamedIndividual student = factory.getOWLNamedIndividual(studentIRI);
        
        // Check if the student already exists
        if (!ontology.containsIndividualInSignature(studentIRI)) {
            // Create a new student instance
            manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(factory.getOWLClass(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#Student")), student));
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#username")), student, username));
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#firstName")), student, firstName));
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#lastName")), student, lastName));
            
            // Set default level and score
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")), student, 1)); // Default level 1
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#score")), student, 0)); // Default score 0
    
            // Log the addition of the new student
            System.out.println("Added new student: " + username);
        } else {
            // Log that the student already exists
            System.out.println("Student already exists: " + username);
        }
    
        // Save the ontology after adding/updating the student
        saveOntology();
    }

    public void updateStudentScoreAndLevel(String username, int newScore, int newLevel) throws OWLOntologyStorageException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
    
        // Create the IRI for the student based on the username
        IRI studentIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#" + username);
        OWLNamedIndividual student = factory.getOWLNamedIndividual(studentIRI);
    
        // Check if the student exists
        if (ontology.containsIndividualInSignature(studentIRI)) {
            // Remove existing score and level axioms
            ontology.axioms(student).forEach(axiom -> {
                if (axiom instanceof OWLDataPropertyAssertionAxiom) {
                    OWLDataPropertyAssertionAxiom dataPropertyAssertion = (OWLDataPropertyAssertionAxiom) axiom;
                    // Check if the property belongs to the current student
                    if (dataPropertyAssertion.getSubject().equals(student) &&
                        (dataPropertyAssertion.getProperty().asOWLDataProperty().getIRI().equals(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#score")).getIRI()) ||
                         dataPropertyAssertion.getProperty().asOWLDataProperty().getIRI().equals(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")).getIRI()))) {
                        // Remove the axiom only if it belongs to the current student
                        manager.removeAxiom(ontology, axiom);
                    }
                }
            });
    
            // Add new score and level
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#score")), student, newScore));
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")), student, newLevel));
    
            // Log the update
            System.out.println("Updated student: " + username + " with score: " + newScore + " and level: " + newLevel);
        } else {
            System.out.println("Student not found in ontology: " + username);
        }
    
        // Save the ontology after updating the score and level
        saveOntology();
    }

    // reset score and level of student
    public void resetStudentScoreAndLevel(String username, int newScore, int newLevel) throws OWLOntologyStorageException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
    
        // Create the IRI for the student based on the username
        IRI studentIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#" + username);
        OWLNamedIndividual student = factory.getOWLNamedIndividual(studentIRI);
    
        // Check if the student exists
        if (ontology.containsIndividualInSignature(studentIRI)) {
            // Remove existing score and level axioms
            ontology.axioms(student).forEach(axiom -> {
                if (axiom instanceof OWLDataPropertyAssertionAxiom) {
                    OWLDataPropertyAssertionAxiom dataPropertyAssertion = (OWLDataPropertyAssertionAxiom) axiom;
                    // Check if the property belongs to the current student
                    if (dataPropertyAssertion.getSubject().equals(student) &&
                        (dataPropertyAssertion.getProperty().asOWLDataProperty().getIRI().equals(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#score")).getIRI()) ||
                         dataPropertyAssertion.getProperty().asOWLDataProperty().getIRI().equals(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")).getIRI()))) {
                        // Remove the axiom only if it belongs to the current student
                        manager.removeAxiom(ontology, axiom);
                    }
                }
            });
    
            // Reset score and level
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#score")), student, 0));
            manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")), student, 1));
    
            // Log the update
            System.out.println("Updated student: " + username + " reset level and score");
        } else {
            System.out.println("Student not found in ontology: " + username);
        }
    
        // Save the ontology after updating the score and level
        saveOntology();
    }

    public void addQuestion(String questionName,String questionText, String correctAnswer, int level) throws OWLOntologyStorageException  {
        

        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
          // Create a new question individual
        IRI questionIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#" + questionName);
        OWLNamedIndividual question = factory.getOWLNamedIndividual(questionIRI);
        
         // Create a new student instance
         manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(factory.getOWLClass(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#Question")), question));
         manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#question")), question, questionText));
         manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#answer")), question, correctAnswer));
         manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#level")), question, level));
         
        
        // Save the ontology after adding the question
        saveOntology();
    }

    private void saveOntology() throws OWLOntologyStorageException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        File file = new File("src/main/resources/areaLearning.owl"); 
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    //getting the data property of instance of rectangle
    public Map<String, String> getRectangleFormulaDataProperties() throws OWLOntologyCreationException {
        Map<String, String> properties = new HashMap<>();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        // Create the IRI for the rectangleformula instance
        IRI rectangleFormulaIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#rectangleformula");
        OWLNamedIndividual rectangleFormula = factory.getOWLNamedIndividual(rectangleFormulaIRI);

        // Check if the instance exists
        if (ontology.containsIndividualInSignature(rectangleFormulaIRI)) {
            // Fetch data properties for the rectangleformula instance
            ontology.dataPropertyAssertionAxioms(rectangleFormula).forEach(axiom -> {
                String propertyName = axiom.getProperty().asOWLDataProperty().getIRI().getShortForm();
                String propertyValue = axiom.getObject().getLiteral();
                properties.put(propertyName, propertyValue);
            });
        } else {
            System.out.println("The individual 'rectangleformula' does not exist in the ontology.");
        }

        return properties;
    }

    //getting the data property of instance of square
    public Map<String, String> getSquareFormulaDataProperties() throws OWLOntologyCreationException {
        Map<String, String> properties = new HashMap<>();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        // Create the IRI for the rectangleformula instance
        IRI squareFormulaIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#squareformula");
        OWLNamedIndividual squareFormula = factory.getOWLNamedIndividual(squareFormulaIRI);

        // Check if the instance exists
        if (ontology.containsIndividualInSignature(squareFormulaIRI)) {
            // Fetch data properties for the squareformula instance
            ontology.dataPropertyAssertionAxioms(squareFormula).forEach(axiom -> {
                String propertyName = axiom.getProperty().asOWLDataProperty().getIRI().getShortForm();
                String propertyValue = axiom.getObject().getLiteral();
                properties.put(propertyName, propertyValue);
            });
        } else {
            System.out.println("The individual 'squareformula' does not exist in the ontology.");
        }

        return properties;
    }

    //getting the data property of instance of circle
    public Map<String, String> getCircleFormulaDataProperties() throws OWLOntologyCreationException {
        Map<String, String> properties = new HashMap<>();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        // Create the IRI for the circleformula instance
        IRI circleFormulaIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#circleformula");
        OWLNamedIndividual circleFormula = factory.getOWLNamedIndividual(circleFormulaIRI);

        // Check if the instance exists
        if (ontology.containsIndividualInSignature(circleFormulaIRI)) {
            // Fetch data properties for the circleformula instance
            ontology.dataPropertyAssertionAxioms(circleFormula).forEach(axiom -> {
                String propertyName = axiom.getProperty().asOWLDataProperty().getIRI().getShortForm();
                String propertyValue = axiom.getObject().getLiteral();
                properties.put(propertyName, propertyValue);
            });
        } else {
            System.out.println("The individual 'squareformula' does not exist in the ontology.");
        }

        return properties;
    }

    //getting the data property of instance of triangle
    public Map<String, String> getTriangleFormulaDataProperties() throws OWLOntologyCreationException {
        Map<String, String> properties = new HashMap<>();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        // Create the IRI for the circleformula instance
        IRI triangleFormulaIRI = IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#triangleFormula");
        OWLNamedIndividual triangleFormula = factory.getOWLNamedIndividual(triangleFormulaIRI);

        // Check if the instance exists
        if (ontology.containsIndividualInSignature(triangleFormulaIRI)) {
            // Fetch data properties for the circleformula instance
            ontology.dataPropertyAssertionAxioms(triangleFormula).forEach(axiom -> {
                String propertyName = axiom.getProperty().asOWLDataProperty().getIRI().getShortForm();
                String propertyValue = axiom.getObject().getLiteral();
                properties.put(propertyName, propertyValue);
            });
        } else {
            System.out.println("The individual 'triangleformula' does not exist in the ontology.");
        }

        return properties;
    }

    //getting the questions
    public List<Question> getQuestionsByLevel(int level) throws OWLOntologyCreationException {
        List<Question> questions = new ArrayList<>();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLClass questionClass = factory.getOWLClass(IRI.create("http://www.semanticweb.org/pratap/ontologies/2024/11/areaLearning#Question"));
    
        // Create a reasoner
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
    
        // Iterate over all individuals in the ontology
        ontology.individualsInSignature().forEach(individual -> {
            // Check if the individual is of type Question using the reasoner
            if (reasoner.getTypes(individual, false).containsEntity(questionClass)) {
                Question question = new Question();
                question.setIriid(individual.getIRI().toString()); // Use the full IRI as an ID
    
                // Fetch the data properties for the individual
                ontology.dataPropertyAssertionAxioms(individual).forEach(axiom -> {
                    String propertyName = axiom.getProperty().asOWLDataProperty().getIRI().getShortForm();
                    String propertyValue = axiom.getObject().getLiteral();
    
                    switch (propertyName) {
                        case "question":
                            question.setQuestion(propertyValue);
                            break;
                        case "answer":
                            question.setAnswer(propertyValue);
                            break;
                        case "level":
                            int questionLevel = Integer.parseInt(propertyValue); // Convert to int
                            question.setLevel(questionLevel); // Assuming setLevel accepts int
                            break;
                    
                    }
                });
                // Add the question to the list if it matches the specified level
                // if (question.getLevel().equals(level)) {
                //     questions.add(question);
                // }

                if(question.getLevel()==level){
                    questions.add(question);
                }
            }
        });
        return questions;
    } 
}