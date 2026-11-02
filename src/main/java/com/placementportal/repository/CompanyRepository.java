package com.placementportal.repository;

import com.placementportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Placement Companies.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByOrderByPackageLpaDesc();
}
