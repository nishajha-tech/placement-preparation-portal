package com.placementportal.controller;

import com.placementportal.dto.EligibilityDTO;
import com.placementportal.entity.User;
import com.placementportal.service.EligibilityService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller handling the Placement Eligibility Checker and What-If Academic Simulator.
 */
@Controller
@RequestMapping("/eligibility")
public class EligibilityController {

    private final EligibilityService eligibilityService;
    private final UserService userService;

    @Autowired
    public EligibilityController(EligibilityService eligibilityService, UserService userService) {
        this.eligibilityService = eligibilityService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String checkEligibility(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        List<EligibilityDTO> results = eligibilityService.checkEligibility(student);

        long eligibleCount = results.stream().filter(EligibilityDTO::isEligible).count();
        long totalCount = results.size();

        model.addAttribute("student", student);
        model.addAttribute("results", results);
        model.addAttribute("eligibleCount", eligibleCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("isSimulated", false);

        return "eligibility";
    }

    @GetMapping("/simulate")
    public String simulateEligibility(@RequestParam("cgpa") Double cgpa,
                                      @RequestParam("tenthPercentage") Double tenthPct,
                                      @RequestParam("twelfthPercentage") Double twelfthPct,
                                      @RequestParam("activeBacklogs") Integer backlogs,
                                      @RequestParam(value = "branch", required = false, defaultValue = "CSE") String branch,
                                      Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);

        List<EligibilityDTO> results = eligibilityService.simulateEligibility(cgpa, tenthPct, twelfthPct, backlogs, branch);

        long eligibleCount = results.stream().filter(EligibilityDTO::isEligible).count();

        model.addAttribute("student", student);
        model.addAttribute("results", results);
        model.addAttribute("eligibleCount", eligibleCount);
        model.addAttribute("totalCount", results.size());
        model.addAttribute("isSimulated", true);
        model.addAttribute("simCgpa", cgpa);
        model.addAttribute("sim10th", tenthPct);
        model.addAttribute("sim12th", twelfthPct);
        model.addAttribute("simBacklogs", backlogs);
        model.addAttribute("simBranch", branch);

        return "eligibility";
    }
}
