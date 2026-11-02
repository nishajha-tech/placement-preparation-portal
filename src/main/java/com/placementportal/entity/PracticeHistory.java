package com.placementportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * PracticeHistory Entity tracking individual aptitude question practice attempts.
 * Feeds accuracy data into the student dashboard and Chart.js analytics.
 */
@Entity
@Table(name = "practice_history")
public class PracticeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 2)
    private String selectedOption;

    @Column(nullable = false)
    private Boolean isCorrect;

    private LocalDateTime practicedAt;

    public PracticeHistory() {
    }

    public PracticeHistory(User student, Question question, String selectedOption, Boolean isCorrect) {
        this.student = student;
        this.question = question;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.practicedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.practicedAt == null) {
            this.practicedAt = LocalDateTime.now();
        }
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public LocalDateTime getPracticedAt() {
        return practicedAt;
    }

    public void setPracticedAt(LocalDateTime practicedAt) {
        this.practicedAt = practicedAt;
    }
}
