package com.placementportal.repository;

import com.placementportal.entity.TestAttempt;
import com.placementportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Mock Test Attempts.
 */
@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByStudentOrderByAttemptedAtDesc(User student);
    List<TestAttempt> findTop5ByStudentOrderByAttemptedAtDesc(User student);
    long countByStudent(User student);
    long countByStudentAndStatus(User student, String status);

    @Query("SELECT AVG(t.percentage) FROM TestAttempt t WHERE t.student = :student")
    Double getAveragePercentageByStudent(User student);

    List<TestAttempt> findAllByOrderByAttemptedAtDesc();
}
