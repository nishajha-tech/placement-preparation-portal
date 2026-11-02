package com.placementportal.controller;

import com.placementportal.entity.Question;
import com.placementportal.entity.User;
import com.placementportal.service.AptitudeService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller handling Aptitude practice, category filtering, and instant answer validation.
 */
@Controller
@RequestMapping("/aptitude")
public class AptitudeController {

    private final AptitudeService aptitudeService;
    private final UserService userService;

    @Autowired
    public AptitudeController(AptitudeService aptitudeService, UserService userService) {
        this.aptitudeService = aptitudeService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String aptitudeHome(@RequestParam(value = "category", required = false, defaultValue = "QUANTITATIVE") String category,
                               @RequestParam(value = "difficulty", required = false, defaultValue = "ALL") String difficulty,
                               Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        List<Question> questions = aptitudeService.getQuestions(category, difficulty);

        model.addAttribute("student", student);
        model.addAttribute("selectedCategory", category.toUpperCase());
        model.addAttribute("selectedDifficulty", difficulty.toUpperCase());
        model.addAttribute("questions", questions);
        model.addAttribute("totalQuestions", aptitudeService.getTotalQuestionCount());

        return "aptitude";
    }

    /**
     * AJAX endpoint: Checks chosen option and returns feedback with detailed explanation.
     */
    @PostMapping("/check-answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAnswer(@RequestParam("questionId") Long questionId,
                                                           @RequestParam("selectedOption") String selectedOption,
                                                           Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        Map<String, Object> result = aptitudeService.checkAnswer(questionId, selectedOption, student);
        return ResponseEntity.ok(result);
    }
}
