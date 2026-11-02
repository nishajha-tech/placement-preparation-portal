package com.placementportal.service;

import com.placementportal.entity.MockTest;
import com.placementportal.entity.Question;
import com.placementportal.entity.TestAttempt;
import com.placementportal.entity.User;
import com.placementportal.repository.MockTestRepository;
import com.placementportal.repository.TestAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service managing timed Mock Tests, automatic grading, time tracking and detailed result review.
 */
@Service
public class MockTestService {

    private final MockTestRepository mockTestRepository;
    private final TestAttemptRepository testAttemptRepository;

    @Autowired
    public MockTestService(MockTestRepository mockTestRepository, TestAttemptRepository testAttemptRepository) {
        this.mockTestRepository = mockTestRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    public List<MockTest> getAllMockTests() {
        return mockTestRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<MockTest> getMockTestById(Long id) {
        return mockTestRepository.findById(id);
    }

    /**
     * Grades a submitted mock test, tracks accuracy, and generates a question review payload.
     */
    @Transactional
    public TestAttempt submitMockTest(Long testId, Map<String, String> submittedAnswers, int timeTakenSeconds, User student) {
        MockTest mockTest = mockTestRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Mock test not found with ID: " + testId));

        List<Question> questions = mockTest.getQuestions();
        int totalQuestions = questions.size();
        int correct = 0;
        int wrong = 0;
        int unattempted = 0;

        // Build review breakdown
        StringBuilder responsesJsonBuilder = new StringBuilder("[");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String questionKey = "q_" + q.getId();
            String selectedOption = submittedAnswers.get(questionKey);

            boolean isAnsCorrect = false;
            if (selectedOption == null || selectedOption.trim().isEmpty() || "NONE".equalsIgnoreCase(selectedOption)) {
                unattempted++;
                selectedOption = "UNATTEMPTED";
            } else if (selectedOption.trim().equalsIgnoreCase(q.getCorrectOption().trim())) {
                correct++;
                isAnsCorrect = true;
            } else {
                wrong++;
            }

            // Append item to JSON array
            responsesJsonBuilder.append(String.format(
                "{\"questionId\":%d,\"selected\":\"%s\",\"correct\":\"%s\",\"isCorrect\":%b}",
                q.getId(), selectedOption, q.getCorrectOption(), isAnsCorrect
            ));
            if (i < questions.size() - 1) {
                responsesJsonBuilder.append(",");
            }
        }
        responsesJsonBuilder.append("]");

        int score = correct; // 1 mark per question
        double percentage = totalQuestions > 0 ? ((double) correct / totalQuestions) * 100.0 : 0.0;
        percentage = Math.round(percentage * 10.0) / 10.0;

        int passCriteria = mockTest.getPassingMarks() != null ? mockTest.getPassingMarks() : (int) Math.ceil(totalQuestions * 0.5);
        String status = (score >= passCriteria) ? "PASSED" : "FAILED";

        TestAttempt attempt = new TestAttempt();
        attempt.setStudent(student);
        attempt.setMockTest(mockTest);
        attempt.setTotalQuestions(totalQuestions);
        attempt.setCorrectAnswers(correct);
        attempt.setWrongAnswers(wrong);
        attempt.setUnattempted(unattempted);
        attempt.setScore(score);
        attempt.setPercentage(percentage);
        attempt.setTimeTakenSeconds(timeTakenSeconds);
        attempt.setStatus(status);
        attempt.setResponsesJson(responsesJsonBuilder.toString());
        attempt.setAttemptedAt(LocalDateTime.now());

        return testAttemptRepository.save(attempt);
    }

    public List<TestAttempt> getAttemptsByStudent(User student) {
        return testAttemptRepository.findByStudentOrderByAttemptedAtDesc(student);
    }

    public List<TestAttempt> getTopRecentAttemptsByStudent(User student) {
        return testAttemptRepository.findTop5ByStudentOrderByAttemptedAtDesc(student);
    }

    public Optional<TestAttempt> getAttemptById(Long id) {
        return testAttemptRepository.findById(id);
    }

    public long getTotalAttemptsByStudent(User student) {
        return testAttemptRepository.countByStudent(student);
    }

    public Double getAverageScoreByStudent(User student) {
        Double avg = testAttemptRepository.getAveragePercentageByStudent(student);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
}
