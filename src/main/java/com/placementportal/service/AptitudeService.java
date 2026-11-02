package com.placementportal.service;

import com.placementportal.entity.PracticeHistory;
import com.placementportal.entity.Question;
import com.placementportal.entity.User;
import com.placementportal.repository.PracticeHistoryRepository;
import com.placementportal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service handling Aptitude practice, category filtering, answer verification and explanation.
 */
@Service
public class AptitudeService {

    private final QuestionRepository questionRepository;
    private final PracticeHistoryRepository practiceHistoryRepository;

    @Autowired
    public AptitudeService(QuestionRepository questionRepository, PracticeHistoryRepository practiceHistoryRepository) {
        this.questionRepository = questionRepository;
        this.practiceHistoryRepository = practiceHistoryRepository;
    }

    public List<Question> getQuestions(String category, String difficulty) {
        if (category != null && !category.isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            if (difficulty != null && !difficulty.isEmpty() && !"ALL".equalsIgnoreCase(difficulty)) {
                return questionRepository.findByCategoryAndDifficulty(category.toUpperCase(), difficulty.toUpperCase());
            }
            return questionRepository.findByCategory(category.toUpperCase());
        } else if (difficulty != null && !difficulty.isEmpty() && !"ALL".equalsIgnoreCase(difficulty)) {
            return questionRepository.findByDifficulty(difficulty.toUpperCase());
        }
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Verifies selected option, saves practice attempt for user analytics, and returns feedback.
     */
    @Transactional
    public Map<String, Object> checkAnswer(Long questionId, String selectedOption, User student) {
        Map<String, Object> response = new HashMap<>();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID: " + questionId));

        boolean isCorrect = question.getCorrectOption().trim().equalsIgnoreCase(selectedOption.trim());

        // Save attempt in PracticeHistory if student is logged in
        if (student != null) {
            PracticeHistory history = new PracticeHistory(student, question, selectedOption.toUpperCase(), isCorrect);
            practiceHistoryRepository.save(history);
        }

        response.put("questionId", questionId);
        response.put("isCorrect", isCorrect);
        response.put("correctOption", question.getCorrectOption());
        response.put("selectedOption", selectedOption.toUpperCase());
        response.put("explanation", question.getExplanation());
        response.put("topic", question.getTopic());

        return response;
    }

    public long getTotalQuestionCount() {
        return questionRepository.count();
    }

    public long getPracticeCountByStudent(User student) {
        return practiceHistoryRepository.countByStudent(student);
    }

    public long getCorrectPracticeCountByStudent(User student) {
        return practiceHistoryRepository.countByStudentAndIsCorrect(student, true);
    }
}
