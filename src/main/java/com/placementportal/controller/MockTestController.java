package com.placementportal.controller;

import com.placementportal.entity.MockTest;
import com.placementportal.entity.Question;
import com.placementportal.entity.TestAttempt;
import com.placementportal.entity.User;
import com.placementportal.service.MockTestService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller managing Timed Mock Tests, test taking interface, auto-submit, and result reviews.
 */
@Controller
@RequestMapping("/mock-test")
public class MockTestController {

    private final MockTestService mockTestService;
    private final UserService userService;

    @Autowired
    public MockTestController(MockTestService mockTestService, UserService userService) {
        this.mockTestService = mockTestService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String listMockTests(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        model.addAttribute("student", student);
        model.addAttribute("mockTests", mockTestService.getAllMockTests());
        return "mock-tests";
    }

    @GetMapping("/{id}/instructions")
    public String testInstructions(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        MockTest test = mockTestService.getMockTestById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID: " + id));

        model.addAttribute("student", student);
        model.addAttribute("test", test);
        return "test-instructions";
    }

    @GetMapping("/{id}/start")
    public String startTest(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        MockTest test = mockTestService.getMockTestById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID: " + id));

        model.addAttribute("student", student);
        model.addAttribute("test", test);
        model.addAttribute("questions", test.getQuestions());
        model.addAttribute("durationSeconds", test.getDurationMinutes() * 60);

        return "test-environment";
    }

    @PostMapping("/{id}/submit")
    public String submitTest(@PathVariable("id") Long id,
                             @RequestParam Map<String, String> allParams,
                             Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        int timeTaken = 0;
        try {
            if (allParams.containsKey("timeTakenSeconds")) {
                timeTaken = Integer.parseInt(allParams.get("timeTakenSeconds"));
            }
        } catch (NumberFormatException ignored) {}

        TestAttempt attempt = mockTestService.submitMockTest(id, allParams, timeTaken, student);
        return "redirect:/mock-test/attempt/" + attempt.getId() + "/result";
    }

    @GetMapping("/attempt/{attemptId}/result")
    public String testResult(@PathVariable("attemptId") Long attemptId, Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        TestAttempt attempt = mockTestService.getAttemptById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found with ID: " + attemptId));

        model.addAttribute("student", student);
        model.addAttribute("attempt", attempt);
        model.addAttribute("test", attempt.getMockTest());
        model.addAttribute("questions", attempt.getMockTest().getQuestions());

        return "test-result";
    }

    @GetMapping("/history")
    public String testHistory(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        model.addAttribute("student", student);
        model.addAttribute("attempts", mockTestService.getAttemptsByStudent(student));
        return "test-history";
    }
}
