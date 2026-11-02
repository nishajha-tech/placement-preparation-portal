package com.placementportal.controller;

import com.placementportal.entity.User;
import com.placementportal.service.AnalyticsService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Controller providing Chart.js visual analytics and placement preparation diagnostics.
 */
@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserService userService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, UserService userService) {
        this.analyticsService = analyticsService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String analyticsPage(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        Map<String, Double> categoryAccuracy = analyticsService.getCategoryAccuracy(student);
        Map<String, Object> testHistory = analyticsService.getMockTestHistoryChart(student);
        Map<String, Integer> codingDist = analyticsService.getCodingSolvedDistribution(student);
        int readinessScore = analyticsService.calculateReadinessScore(student);
        Map<String, String> insights = analyticsService.getPerformanceInsights(student);

        model.addAttribute("student", student);
        model.addAttribute("readinessScore", readinessScore);
        model.addAttribute("categoryAccuracy", categoryAccuracy);
        model.addAttribute("testHistoryLabels", testHistory.get("labels"));
        model.addAttribute("testHistoryScores", testHistory.get("scores"));
        model.addAttribute("codingDist", codingDist);
        model.addAttribute("insights", insights);

        return "analytics";
    }
}
