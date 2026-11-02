package com.placementportal.service;

import com.placementportal.entity.Company;
import com.placementportal.entity.MockTest;
import com.placementportal.entity.Question;
import com.placementportal.entity.User;
import com.placementportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service providing administrative functions for managing questions, tests, companies, and students.
 */
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final MockTestRepository mockTestRepository;
    private final CompanyRepository companyRepository;
    private final TestAttemptRepository testAttemptRepository;

    @Autowired
    public AdminService(UserRepository userRepository,
                        QuestionRepository questionRepository,
                        MockTestRepository mockTestRepository,
                        CompanyRepository companyRepository,
                        TestAttemptRepository testAttemptRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.mockTestRepository = mockTestRepository;
        this.companyRepository = companyRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    public Map<String, Long> getAdminStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", userRepository.countByRole("ROLE_STUDENT"));
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalMockTests", mockTestRepository.count());
        stats.put("totalCompanies", companyRepository.count());
        stats.put("totalAttempts", testAttemptRepository.count());
        return stats;
    }

    // Question Management
    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    // Mock Test Management
    @Transactional
    public MockTest saveMockTest(MockTest mockTest, List<Long> questionIds) {
        if (questionIds != null && !questionIds.isEmpty()) {
            List<Question> questions = questionRepository.findAllById(questionIds);
            mockTest.setQuestions(questions);
            mockTest.setTotalMarks(questions.size());
            if (mockTest.getPassingMarks() == null) {
                mockTest.setPassingMarks((int) Math.ceil(questions.size() * 0.5));
            }
        }
        return mockTestRepository.save(mockTest);
    }

    @Transactional
    public void deleteMockTest(Long id) {
        mockTestRepository.deleteById(id);
    }

    // Company Management
    @Transactional
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    // Student Management
    public List<User> getAllStudents() {
        return userRepository.findByRole("ROLE_STUDENT");
    }

    @Transactional
    public void deleteStudent(Long id) {
        userRepository.deleteById(id);
    }
}
