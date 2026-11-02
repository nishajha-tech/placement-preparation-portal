package com.placementportal.controller;

import com.placementportal.entity.User;
import com.placementportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling Student Dashboard overview, statistics, and profile management.
 */
@Controller
public class DashboardController {

    private final UserService userService;
    private final MockTestService mockTestService;
    private final AptitudeService aptitudeService;
    private final CodingService codingService;
    private final EligibilityService eligibilityService;
    private final AnalyticsService analyticsService;

    @Autowired
    public DashboardController(UserService userService,
                               MockTestService mockTestService,
                               AptitudeService aptitudeService,
                               CodingService codingService,
                               EligibilityService eligibilityService,
                               AnalyticsService analyticsService) {
        this.userService = userService;
        this.mockTestService = mockTestService;
        this.aptitudeService = aptitudeService;
        this.codingService = codingService;
        this.eligibilityService = eligibilityService;
        this.analyticsService = analyticsService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", student);
        model.addAttribute("readinessScore", analyticsService.calculateReadinessScore(student));
        model.addAttribute("testsAttempted", mockTestService.getTotalAttemptsByStudent(student));
        model.addAttribute("averageScore", mockTestService.getAverageScoreByStudent(student));
        model.addAttribute("practiceCount", aptitudeService.getPracticeCountByStudent(student));
        model.addAttribute("codingSolvedCount", codingService.getSolvedCountForStudent(student));
        model.addAttribute("eligibleCompaniesCount", eligibilityService.countEligibleCompanies(student));
        model.addAttribute("recentAttempts", mockTestService.getTopRecentAttemptsByStudent(student));
        model.addAttribute("insights", analyticsService.getPerformanceInsights(student));
        model.addAttribute("companies", eligibilityService.getAllCompanies());

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        model.addAttribute("student", student);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("student") User updatedData,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        userService.updateProfile(student, updatedData);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully! Placement eligibility recalculated.");
        return "redirect:/profile";
    }
}
