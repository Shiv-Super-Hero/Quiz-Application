package com.shivaji.service;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shivaji.model.Question;
import com.shivaji.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	
	private final QuestionRepository questionRepository;

	@Override
	public Question createQuestion(Question question) {
		
		return questionRepository.save(question);
	}

	@Override
	public List<Question> getAllQuestions() {
		
		return questionRepository.findAll();
	}

	@Override
	public Optional<Question> getQuestionById(Long id) {
		
		return questionRepository.findById(id);
	}

	@Override
	public List<String> getAllSubjects() {
		
		return questionRepository.findDistinctSubjects();
	}

	@Override
	public Question updateQuestion(Long id, Question question)throws ChangeSetPersister.NotFoundException{
		
		Optional<Question> theQuestion = this.getQuestionById(id);
		
		if(theQuestion.isPresent()) {
			Question updatedQuestion = theQuestion.get();
			updatedQuestion.setQuestion(question.getQuestion());
			updatedQuestion.setChoices(question.getChoices());
			updatedQuestion.setCorrectAnswers(question.getCorrectAnswers());
			return questionRepository.save(updatedQuestion);
		}
		else {
			throw new ChangeSetPersister.NotFoundException();
		}
	}

	@Override
	public void deleteQuestion(Long id) {
		
		questionRepository.deleteById(id);
	}

	@Override
	public List<Question> getQuestionsForUser(Integer numOfQuestions, String subject) {
		Pageable pageable = PageRequest.of(0,numOfQuestions);
		return questionRepository.findBySubject(subject,pageable).getContent();
		
	}

}
