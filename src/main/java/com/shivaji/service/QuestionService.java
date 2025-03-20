package com.shivaji.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.shivaji.model.Question;

public interface QuestionService {
	
	Question createQuestion(Question question);
	
	List<Question> getAllQuestions();
	
	Optional<Question> getQuestionById(Long id);
	
	List<String> getAllSubjects();
	
	Question updateQuestion(Long id,Question question) throws NotFoundException;
	
	void deleteQuestion(Long id);
	
	List<Question> getQuestionsForUser(Integer numOfQuestions,String subject);
}
