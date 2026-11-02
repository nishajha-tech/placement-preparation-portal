package com.placementportal.repository;

import com.placementportal.entity.MockTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Mock Tests.
 */
@Repository
public interface MockTestRepository extends JpaRepository<MockTest, Long> {
    List<MockTest> findAllByOrderByCreatedAtDesc();
    List<MockTest> findByCategory(String category);
    List<MockTest> findByCompanyTag(String companyTag);
}
