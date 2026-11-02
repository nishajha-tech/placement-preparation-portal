package com.placementportal.repository;

import com.placementportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Question management and filtering.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
    List<Question> findByCategoryAndDifficulty(String category, String difficulty);
    List<Question> findByDifficulty(String difficulty);
    long countByCategory(String category);

    @Query("SELECT DISTINCT q.category FROM Question q")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT q.topic FROM Question q WHERE q.category = :category")
    List<String> findTopicsByCategory(String category);
}
