package com.placementportal.repository;

import com.placementportal.entity.CodeSubmission;
import com.placementportal.entity.CodingProblem;
import com.placementportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Code Submissions.
 */
@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    List<CodeSubmission> findByStudentOrderBySubmittedAtDesc(User student);
    List<CodeSubmission> findByStudentAndProblemOrderBySubmittedAtDesc(User student, CodingProblem problem);
    long countByStudentAndStatus(User student, String status);

    @Query("SELECT COUNT(DISTINCT s.problem.id) FROM CodeSubmission s WHERE s.student = :student AND s.status = 'ACCEPTED'")
    long countDistinctAcceptedProblemsByStudent(User student);

    @Query("SELECT DISTINCT s.problem.id FROM CodeSubmission s WHERE s.student = :student AND s.status = 'ACCEPTED'")
    List<Long> findAcceptedProblemIdsByStudent(User student);
}
