package com.shivaji.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shivaji.model.Question;
import com.shivaji.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:5173") 
public class QuestionController {
	
	private final QuestionService questionService;
	
	@PostMapping("/create-new-question")
	public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question){
		
		Question createdQuestion = questionService.createQuestion(question);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
	}
	
	@GetMapping("/all-questions")
	public ResponseEntity<List<Question>> getAllQuestions(){
		
		List<Question> questions = questionService.getAllQuestions();
		return ResponseEntity.ok(questions);
	}
	
	@GetMapping("/question/{id}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException{
		
		Optional<Question> theQuestion = questionService.getQuestionById(id);
		if(theQuestion.isPresent()) {
			return ResponseEntity.ok(theQuestion.get());
		}
		else {
			throw new ChangeSetPersister.NotFoundException();
		}
	}
	
	@PutMapping("/question/{id}/update")
	public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) throws NotFoundException{
		
		Question updatedQuestion = questionService.updateQuestion(id, question);
		return ResponseEntity.ok(updatedQuestion);
	}
	
	@DeleteMapping("/question/{id}/delete")
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id){
		
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/subjects")
	public ResponseEntity<List<String>> getAllSubjects(){
		List<String> subjects = questionService.getAllSubjects();
		return ResponseEntity.ok(subjects);
	}
	
	@GetMapping("/quiz/fetch-questions-for-user")
	public ResponseEntity<List<Question>> getQuestionsForUser(@RequestParam Integer numOFQuestions,@RequestParam String subject){
		List<Question> allQuestions = questionService.getQuestionsForUser(numOFQuestions, subject);
		List<Question> mutableQuestion = new ArrayList<>(allQuestions);
		Collections.shuffle(mutableQuestion);
		
		int availableQuestions = Math.min(numOFQuestions, mutableQuestion.size());
		List<Question> randomQuestions = mutableQuestion.subList(0, availableQuestions);
		return ResponseEntity.ok(randomQuestions);
	}
}
