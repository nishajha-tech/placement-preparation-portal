package com.placementportal.controller;

import com.placementportal.entity.CodeSubmission;
import com.placementportal.entity.CodingProblem;
import com.placementportal.entity.User;
import com.placementportal.service.CodingService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller handling DSA & Java coding challenges, live code editing, and sandboxed execution.
 */
@Controller
@RequestMapping("/coding")
public class CodingController {

    private final CodingService codingService;
    private final UserService userService;

    @Autowired
    public CodingController(CodingService codingService, UserService userService) {
        this.codingService = codingService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String codingList(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        List<CodingProblem> problems = codingService.getAllProblems();
        List<Long> solvedIds = codingService.getSolvedProblemIdsForStudent(student);

        model.addAttribute("student", student);
        model.addAttribute("problems", problems);
        model.addAttribute("solvedIds", solvedIds);
        model.addAttribute("solvedCount", solvedIds.size());

        return "coding-list";
    }

    @GetMapping("/{id}")
    public String codingEditor(@PathVariable("id") Long id, Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        CodingProblem problem = codingService.getProblemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with ID: " + id));

        model.addAttribute("student", student);
        model.addAttribute("problem", problem);

        return "coding-editor";
    }

    /**
     * AJAX endpoint: Executes code against sample test cases without persisting a final submission.
     */
    @PostMapping("/run")
    @ResponseBody
    public ResponseEntity<CodingService.EvaluationResult> runCode(@RequestBody Map<String, Object> payload) {
        Long problemId = Long.parseLong(payload.get("problemId").toString());
        String code = (String) payload.get("sourceCode");

        CodingProblem problem = codingService.getProblemById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with ID: " + problemId));

        CodingService.EvaluationResult result = codingService.evaluateCode(problem, code);
        return ResponseEntity.ok(result);
    }

    /**
     * AJAX endpoint: Evaluates code against all test cases and persists submission.
     */
    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitCode(@RequestBody Map<String, Object> payload,
                                                          Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        Long problemId = Long.parseLong(payload.get("problemId").toString());
        String code = (String) payload.get("sourceCode");

        CodeSubmission submission = codingService.submitSolution(problemId, code, student);

        Map<String, Object> res = new HashMap<>();
        res.put("status", submission.getStatus());
        res.put("passed", submission.getTestCasesPassed());
        res.put("total", submission.getTotalTestCases());
        res.put("output", submission.getExecutionOutput());

        return ResponseEntity.ok(res);
    }
}
