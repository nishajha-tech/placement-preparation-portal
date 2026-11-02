package com.placementportal.repository;

import com.placementportal.entity.CodingProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Coding Problems.
 */
@Repository
public interface CodingProblemRepository extends JpaRepository<CodingProblem, Long> {
    List<CodingProblem> findAllByOrderByDifficultyAsc();
    List<CodingProblem> findByCategory(String category);
    List<CodingProblem> findByDifficulty(String difficulty);
}
