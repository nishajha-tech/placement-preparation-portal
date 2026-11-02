package com.placementportal.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Company Entity storing campus recruiter criteria and eligibility rules.
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String roleName;

    @Column(nullable = false)
    private Double packageLpa; // e.g. 7.5 (in Lakhs Per Annum)

    @Column(nullable = false)
    private Double minCgpa;

    @Column(nullable = false)
    private Double minTenthPercentage;

    @Column(nullable = false)
    private Double minTwelfthPercentage;

    @Column(nullable = false)
    private Integer maxBacklogs; // Maximum allowed standing backlogs (0, 1, etc.)

    private String eligibleBranches; // e.g. "CSE, IT, ECE, EEE" or "ALL"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String hiringProcess; // e.g. "Aptitude Round -> Technical Coding -> HR Interview"

    private LocalDate applicationDeadline;
    private String applyLink;
    private String logoUrl;

    private LocalDateTime createdAt;

    public Company() {
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Double getPackageLpa() {
        return packageLpa;
    }

    public void setPackageLpa(Double packageLpa) {
        this.packageLpa = packageLpa;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public Double getMinTenthPercentage() {
        return minTenthPercentage;
    }

    public void setMinTenthPercentage(Double minTenthPercentage) {
        this.minTenthPercentage = minTenthPercentage;
    }

    public Double getMinTwelfthPercentage() {
        return minTwelfthPercentage;
    }

    public void setMinTwelfthPercentage(Double minTwelfthPercentage) {
        this.minTwelfthPercentage = minTwelfthPercentage;
    }

    public Integer getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(Integer maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public String getEligibleBranches() {
        return eligibleBranches;
    }

    public void setEligibleBranches(String eligibleBranches) {
        this.eligibleBranches = eligibleBranches;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHiringProcess() {
        return hiringProcess;
    }

    public void setHiringProcess(String hiringProcess) {
        this.hiringProcess = hiringProcess;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getApplyLink() {
        return applyLink;
    }

    public void setApplyLink(String applyLink) {
        this.applyLink = applyLink;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
