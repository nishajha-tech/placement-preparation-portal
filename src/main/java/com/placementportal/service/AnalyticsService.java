package com.placementportal.service;

import com.placementportal.entity.TestAttempt;
import com.placementportal.entity.User;
import com.placementportal.repository.CodeSubmissionRepository;
import com.placementportal.repository.CodingProblemRepository;
import com.placementportal.repository.PracticeHistoryRepository;
import com.placementportal.repository.TestAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service calculating student performance statistics, readiness score,
 * and Chart.js analytical datasets.
 */
@Service
public class AnalyticsService {

    private final PracticeHistoryRepository practiceHistoryRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final CodingProblemRepository codingProblemRepository;

    @Autowired
    public AnalyticsService(PracticeHistoryRepository practiceHistoryRepository,
                            TestAttemptRepository testAttemptRepository,
                            CodeSubmissionRepository codeSubmissionRepository,
                            CodingProblemRepository codingProblemRepository) {
        this.practiceHistoryRepository = practiceHistoryRepository;
        this.testAttemptRepository = testAttemptRepository;
        this.codeSubmissionRepository = codeSubmissionRepository;
        this.codingProblemRepository = codingProblemRepository;
    }

    /**
     * Category-wise accuracy map for Radar / Bar Chart.js.
     */
    public Map<String, Double> getCategoryAccuracy(User student) {
        Map<String, Double> map = new LinkedHashMap<>();
        String[] categories = {"QUANTITATIVE", "LOGICAL", "VERBAL", "TECHNICAL"};

        for (String cat : categories) {
            long total = practiceHistoryRepository.countByStudentAndCategory(student, cat);
            long correct = practiceHistoryRepository.countCorrectByStudentAndCategory(student, cat);
            double accuracy = total > 0 ? ((double) correct / total) * 100.0 : 75.0; // Default baseline for demo
            map.put(cat, Math.round(accuracy * 10.0) / 10.0);
        }
        return map;
    }

    /**
     * Mock Test history timeline for Line Chart.js.
     */
    public Map<String, Object> getMockTestHistoryChart(User student) {
        List<TestAttempt> attempts = testAttemptRepository.findByStudentOrderByAttemptedAtDesc(student);
        Collections.reverse(attempts); // Chronological order

        List<String> labels = new ArrayList<>();
        List<Double> scores = new ArrayList<>();

        if (attempts.isEmpty()) {
            // Seed mock progression points for rich first-glance demo
            labels.addAll(Arrays.asList("Diagnostic Test", "Aptitude Round 1", "TCS Mock 1", "Infosys Mock"));
            scores.addAll(Arrays.asList(60.0, 72.0, 80.0, 85.0));
        } else {
            int count = 1;
            for (TestAttempt a : attempts) {
                String testName = a.getMockTest() != null ? a.getMockTest().getTitle() : ("Test #" + count);
                if (testName.length() > 18) {
                    testName = testName.substring(0, 15) + "...";
                }
                labels.add(testName);
                scores.add(a.getPercentage() != null ? a.getPercentage() : 0.0);
                count++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("scores", scores);
        return result;
    }

    /**
     * Coding problems solved distribution (Easy, Medium, Hard) for Doughnut Chart.js.
     */
    public Map<String, Integer> getCodingSolvedDistribution(User student) {
        Map<String, Integer> dist = new LinkedHashMap<>();
        long totalSolved = codeSubmissionRepository.countDistinctAcceptedProblemsByStudent(student);

        if (totalSolved == 0) {
            // Demo default distribution
            dist.put("Easy", 4);
            dist.put("Medium", 3);
            dist.put("Hard", 1);
        } else {
            // Approximate breakdown
            int easy = (int) Math.ceil(totalSolved * 0.5);
            int med = (int) Math.floor(totalSolved * 0.35);
            int hard = (int) Math.max(0, totalSolved - easy - med);
            dist.put("Easy", Math.max(1, easy));
            dist.put("Medium", Math.max(0, med));
            dist.put("Hard", Math.max(0, hard));
        }
        return dist;
    }

    /**
     * Calculates composite Placement Readiness Score (0 to 100).
     */
    public int calculateReadinessScore(User student) {
        if (student == null) return 50;

        double score = 0;

        // 1. Academic weight (max 25 pts)
        double cgpa = student.getCgpa() != null ? student.getCgpa() : 7.0;
        int backlogs = student.getActiveBacklogs() != null ? student.getActiveBacklogs() : 0;
        score += Math.min(20.0, (cgpa / 10.0) * 20.0);
        if (backlogs == 0) score += 5.0;

        // 2. Mock Test weight (max 30 pts)
        Double avgTest = testAttemptRepository.getAveragePercentageByStudent(student);
        if (avgTest != null && avgTest > 0) {
            score += (avgTest / 100.0) * 30.0;
        } else {
            score += 18.0; // Baseline
        }

        // 3. Coding solved weight (max 25 pts)
        long solvedProblems = codeSubmissionRepository.countDistinctAcceptedProblemsByStudent(student);
        score += Math.min(25.0, solvedProblems * 5.0 + 10.0);

        // 4. Aptitude practice activity (max 20 pts)
        long practiceCount = practiceHistoryRepository.countByStudent(student);
        score += Math.min(20.0, practiceCount * 1.5 + 8.0);

        return (int) Math.min(100, Math.round(score));
    }

    /**
     * Generates personalized performance advice and focal areas.
     */
    public Map<String, String> getPerformanceInsights(User student) {
        Map<String, String> insights = new HashMap<>();
        Map<String, Double> acc = getCategoryAccuracy(student);

        String bestCat = "QUANTITATIVE";
        double maxAcc = -1;
        String weakCat = "VERBAL";
        double minAcc = 101;

        for (Map.Entry<String, Double> entry : acc.entrySet()) {
            if (entry.getValue() > maxAcc) {
                maxAcc = entry.getValue();
                bestCat = entry.getKey();
            }
            if (entry.getValue() < minAcc) {
                minAcc = entry.getValue();
                weakCat = entry.getKey();
            }
        }

        insights.put("strongArea", bestCat + " (" + (int)maxAcc + "% accuracy)");
        insights.put("weakArea", weakCat + " (" + (int)minAcc + "% accuracy)");
        insights.put("nextStep", "Take a timed mock test focused on " + weakCat + " to boost overall campus readiness!");

        return insights;
    }
}
