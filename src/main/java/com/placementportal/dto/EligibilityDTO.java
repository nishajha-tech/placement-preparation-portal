package com.placementportal.dto;

import com.placementportal.entity.Company;
import java.util.List;

/**
 * Data Transfer Object holding company eligibility evaluation details.
 */
public class EligibilityDTO {
    private Company company;
    private boolean eligible;
    private List<String> passedCriteria;
    private List<String> failedCriteria;

    public EligibilityDTO(Company company, boolean eligible, List<String> passedCriteria, List<String> failedCriteria) {
        this.company = company;
        this.eligible = eligible;
        this.passedCriteria = passedCriteria;
        this.failedCriteria = failedCriteria;
    }

    public Company getCompany() {
        return company;
    }

    public boolean isEligible() {
        return eligible;
    }

    public List<String> getPassedCriteria() {
        return passedCriteria;
    }

    public List<String> getFailedCriteria() {
        return failedCriteria;
    }
}
