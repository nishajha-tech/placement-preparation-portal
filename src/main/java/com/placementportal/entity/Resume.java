package com.placementportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Resume Entity representing student resume data for the Resume Builder and PDF export.
 */
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User student;

    // Contact Information
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;

    // Professional Summary
    @Column(columnDefinition = "TEXT")
    private String summary;

    // Higher Education
    private String degree;
    private String branch;
    private String college;
    private Double cgpa;
    private Integer passingYear;

    // Schooling
    private String twelfthSchool;
    private Double twelfthPercentage;
    private Integer twelfthYear;

    private String tenthSchool;
    private Double tenthPercentage;
    private Integer tenthYear;

    // Skills
    @Column(columnDefinition = "TEXT")
    private String technicalSkills;

    @Column(columnDefinition = "TEXT")
    private String softSkills;

    @Column(columnDefinition = "TEXT")
    private String toolsAndDatabases;

    // Projects
    private String project1Title;
    private String project1Tech;
    @Column(columnDefinition = "TEXT")
    private String project1Desc;
    private String project1Link;

    private String project2Title;
    private String project2Tech;
    @Column(columnDefinition = "TEXT")
    private String project2Desc;
    private String project2Link;

    // Experience / Internship
    private String expRole;
    private String expCompany;
    private String expDuration;
    @Column(columnDefinition = "TEXT")
    private String expDesc;

    // Certifications & Achievements
    @Column(columnDefinition = "TEXT")
    private String certifications;

    @Column(columnDefinition = "TEXT")
    private String achievements;

    private LocalDateTime updatedAt;

    public Resume() {
    }

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }

    public String getTwelfthSchool() {
        return twelfthSchool;
    }

    public void setTwelfthSchool(String twelfthSchool) {
        this.twelfthSchool = twelfthSchool;
    }

    public Double getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(Double twelfthPercentage) {
        this.twelfthPercentage = twelfthPercentage;
    }

    public Integer getTwelfthYear() {
        return twelfthYear;
    }

    public void setTwelfthYear(Integer twelfthYear) {
        this.twelfthYear = twelfthYear;
    }

    public String getTenthSchool() {
        return tenthSchool;
    }

    public void setTenthSchool(String tenthSchool) {
        this.tenthSchool = tenthSchool;
    }

    public Double getTenthPercentage() {
        return tenthPercentage;
    }

    public void setTenthPercentage(Double tenthPercentage) {
        this.tenthPercentage = tenthPercentage;
    }

    public Integer getTenthYear() {
        return tenthYear;
    }

    public void setTenthYear(Integer tenthYear) {
        this.tenthYear = tenthYear;
    }

    public String getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(String technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public String getSoftSkills() {
        return softSkills;
    }

    public void setSoftSkills(String softSkills) {
        this.softSkills = softSkills;
    }

    public String getToolsAndDatabases() {
        return toolsAndDatabases;
    }

    public void setToolsAndDatabases(String toolsAndDatabases) {
        this.toolsAndDatabases = toolsAndDatabases;
    }

    public String getProject1Title() {
        return project1Title;
    }

    public void setProject1Title(String project1Title) {
        this.project1Title = project1Title;
    }

    public String getProject1Tech() {
        return project1Tech;
    }

    public void setProject1Tech(String project1Tech) {
        this.project1Tech = project1Tech;
    }

    public String getProject1Desc() {
        return project1Desc;
    }

    public void setProject1Desc(String project1Desc) {
        this.project1Desc = project1Desc;
    }

    public String getProject1Link() {
        return project1Link;
    }

    public void setProject1Link(String project1Link) {
        this.project1Link = project1Link;
    }

    public String getProject2Title() {
        return project2Title;
    }

    public void setProject2Title(String project2Title) {
        this.project2Title = project2Title;
    }

    public String getProject2Tech() {
        return project2Tech;
    }

    public void setProject2Tech(String project2Tech) {
        this.project2Tech = project2Tech;
    }

    public String getProject2Desc() {
        return project2Desc;
    }

    public void setProject2Desc(String project2Desc) {
        this.project2Desc = project2Desc;
    }

    public String getProject2Link() {
        return project2Link;
    }

    public void setProject2Link(String project2Link) {
        this.project2Link = project2Link;
    }

    public String getExpRole() {
        return expRole;
    }

    public void setExpRole(String expRole) {
        this.expRole = expRole;
    }

    public String getExpCompany() {
        return expCompany;
    }

    public void setExpCompany(String expCompany) {
        this.expCompany = expCompany;
    }

    public String getExpDuration() {
        return expDuration;
    }

    public void setExpDuration(String expDuration) {
        this.expDuration = expDuration;
    }

    public String getExpDesc() {
        return expDesc;
    }

    public void setExpDesc(String expDesc) {
        this.expDesc = expDesc;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
