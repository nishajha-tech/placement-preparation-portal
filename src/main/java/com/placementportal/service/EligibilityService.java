package com.placementportal.service;

import com.placementportal.dto.EligibilityDTO;
import com.placementportal.entity.Company;
import com.placementportal.entity.User;
import com.placementportal.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service evaluating student placement eligibility against company hiring criteria.
 */
@Service
public class EligibilityService {

    private final CompanyRepository companyRepository;

    @Autowired
    public EligibilityService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAllByOrderByPackageLpaDesc();
    }

    /**
     * Checks student's profile against all company hiring requirements.
     */
    public List<EligibilityDTO> checkEligibility(User student) {
        List<Company> companies = companyRepository.findAllByOrderByPackageLpaDesc();
        List<EligibilityDTO> results = new ArrayList<>();

        for (Company company : companies) {
            results.add(evaluateSingleCompany(student, company));
        }

        return results;
    }

    /**
     * Evaluates what-if scenario with simulated academic inputs.
     */
    public List<EligibilityDTO> simulateEligibility(Double cgpa, Double tenthPct, Double twelfthPct, Integer backlogs, String branch) {
        User simulatedUser = new User();
        simulatedUser.setCgpa(cgpa != null ? cgpa : 0.0);
        simulatedUser.setTenthPercentage(tenthPct != null ? tenthPct : 0.0);
        simulatedUser.setTwelfthPercentage(twelfthPct != null ? twelfthPct : 0.0);
        simulatedUser.setActiveBacklogs(backlogs != null ? backlogs : 0);
        simulatedUser.setBranch(branch != null ? branch : "CSE");

        return checkEligibility(simulatedUser);
    }

    public long countEligibleCompanies(User student) {
        if (student == null) return 0;
        return checkEligibility(student).stream().filter(EligibilityDTO::isEligible).count();
    }

    private EligibilityDTO evaluateSingleCompany(User student, Company company) {
        List<String> passed = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        double userCgpa = student.getCgpa() != null ? student.getCgpa() : 0.0;
        double user10th = student.getTenthPercentage() != null ? student.getTenthPercentage() : 0.0;
        double user12th = student.getTwelfthPercentage() != null ? student.getTwelfthPercentage() : 0.0;
        int userBacklogs = student.getActiveBacklogs() != null ? student.getActiveBacklogs() : 0;
        String userBranch = student.getBranch() != null ? student.getBranch().toUpperCase() : "";

        // 1. CGPA Criterion
        if (userCgpa >= company.getMinCgpa()) {
            passed.add(String.format("CGPA Criterion Passed: Your %.2f >= Required %.2f", userCgpa, company.getMinCgpa()));
        } else {
            failed.add(String.format("Minimum CGPA required is %.2f (Your CGPA is %.2f)", company.getMinCgpa(), userCgpa));
        }

        // 2. 10th Percentage Criterion
        if (user10th >= company.getMinTenthPercentage()) {
            passed.add(String.format("10th Standard Criterion Passed: Your %.1f%% >= Required %.1f%%", user10th, company.getMinTenthPercentage()));
        } else {
            failed.add(String.format("Minimum 10th required is %.1f%% (You have %.1f%%)", company.getMinTenthPercentage(), user10th));
        }

        // 3. 12th Percentage Criterion
        if (user12th >= company.getMinTwelfthPercentage()) {
            passed.add(String.format("12th Standard Criterion Passed: Your %.1f%% >= Required %.1f%%", user12th, company.getMinTwelfthPercentage()));
        } else {
            failed.add(String.format("Minimum 12th required is %.1f%% (You have %.1f%%)", company.getMinTwelfthPercentage(), user12th));
        }

        // 4. Backlog Criterion
        if (userBacklogs <= company.getMaxBacklogs()) {
            passed.add(String.format("Backlogs Criterion Passed: Your %d <= Max allowed %d", userBacklogs, company.getMaxBacklogs()));
        } else {
            failed.add(String.format("Maximum allowed standing backlogs is %d (You have %d)", company.getMaxBacklogs(), userBacklogs));
        }

        // 5. Branch Criterion
        if (company.getEligibleBranches() == null || company.getEligibleBranches().equalsIgnoreCase("ALL") || userBranch.isEmpty()) {
            passed.add("Eligible for all academic engineering branches");
        } else {
            String branches = company.getEligibleBranches().toUpperCase();
            boolean branchMatch = branches.contains(userBranch) || userBranch.contains("CSE") || userBranch.contains("COMPUTER");
            if (branchMatch) {
                passed.add("Degree/Branch eligible: " + student.getBranch());
            } else {
                failed.add("Eligible branches: " + company.getEligibleBranches() + " (Your branch is " + student.getBranch() + ")");
            }
        }

        boolean isEligible = failed.isEmpty();
        return new EligibilityDTO(company, isEligible, passed, failed);
    }
}
