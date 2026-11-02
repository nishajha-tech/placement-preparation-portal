package com.placementportal.repository;

import com.placementportal.entity.PracticeHistory;
import com.placementportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Practice Question History.
 */
@Repository
public interface PracticeHistoryRepository extends JpaRepository<PracticeHistory, Long> {
    List<PracticeHistory> findByStudentOrderByPracticedAtDesc(User student);
    long countByStudent(User student);
    long countByStudentAndIsCorrect(User student, Boolean isCorrect);

    @Query("SELECT COUNT(p) FROM PracticeHistory p WHERE p.student = :student AND p.question.category = :category")
    long countByStudentAndCategory(User student, String category);

    @Query("SELECT COUNT(p) FROM PracticeHistory p WHERE p.student = :student AND p.question.category = :category AND p.isCorrect = true")
    long countCorrectByStudentAndCategory(User student, String category);
}
