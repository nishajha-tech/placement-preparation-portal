package com.placementportal.controller;

import com.placementportal.entity.Company;
import com.placementportal.entity.MockTest;
import com.placementportal.entity.Question;
import com.placementportal.entity.User;
import com.placementportal.service.AdminService;
import com.placementportal.service.AptitudeService;
import com.placementportal.service.EligibilityService;
import com.placementportal.service.MockTestService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller handling the Administrator Panel: Student management,
 * Question Bank management, Mock Tests creation, and Recruiter Company listings.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AptitudeService aptitudeService;
    private final MockTestService mockTestService;
    private final EligibilityService eligibilityService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService,
                           AptitudeService aptitudeService,
                           MockTestService mockTestService,
                           EligibilityService eligibilityService,
                           UserService userService) {
        this.adminService = adminService;
        this.aptitudeService = aptitudeService;
        this.mockTestService = mockTestService;
        this.eligibilityService = eligibilityService;
        this.userService = userService;
    }

    private User getAuthenticatedAdmin(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping({"", "/dashboard"})
    public String adminDashboard(Model model, Authentication authentication) {
        User admin = getAuthenticatedAdmin(authentication);
        model.addAttribute("admin", admin);
        model.addAttribute("stats", adminService.getAdminStats());
        model.addAttribute("recentStudents", adminService.getAllStudents());
        return "admin/dashboard";
    }

    // --- STUDENT MANAGEMENT ---
    @GetMapping("/students")
    public String manageStudents(Model model, Authentication authentication) {
        model.addAttribute("admin", getAuthenticatedAdmin(authentication));
        model.addAttribute("students", adminService.getAllStudents());
        return "admin/students";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student record removed successfully.");
        return "redirect:/admin/students";
    }

    // --- QUESTION MANAGEMENT ---
    @GetMapping("/questions")
    public String manageQuestions(@RequestParam(value = "category", required = false, defaultValue = "ALL") String category,
                                  Model model, Authentication authentication) {
        model.addAttribute("admin", getAuthenticatedAdmin(authentication));
        model.addAttribute("questions", aptitudeService.getQuestions(category, "ALL"));
        model.addAttribute("selectedCategory", category);
        model.addAttribute("newQuestion", new Question());
        return "admin/questions";
    }

    @PostMapping("/questions/add")
    public String addQuestion(@ModelAttribute("newQuestion") Question question, RedirectAttributes redirectAttributes) {
        adminService.saveQuestion(question);
        redirectAttributes.addFlashAttribute("successMessage", "Question added to Question Bank successfully!");
        return "redirect:/admin/questions";
    }

    @PostMapping("/questions/{id}/delete")
    public String deleteQuestion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteQuestion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Question deleted successfully.");
        return "redirect:/admin/questions";
    }

    // --- MOCK TEST MANAGEMENT ---
    @GetMapping("/tests")
    public String manageMockTests(Model model, Authentication authentication) {
        model.addAttribute("admin", getAuthenticatedAdmin(authentication));
        model.addAttribute("tests", mockTestService.getAllMockTests());
        model.addAttribute("allQuestions", aptitudeService.getQuestions("ALL", "ALL"));
        model.addAttribute("newTest", new MockTest());
        return "admin/tests";
    }

    @PostMapping("/tests/add")
    public String addMockTest(@ModelAttribute("newTest") MockTest test,
                              @RequestParam(value = "selectedQuestionIds", required = false) List<Long> questionIds,
                              RedirectAttributes redirectAttributes) {
        adminService.saveMockTest(test, questionIds);
        redirectAttributes.addFlashAttribute("successMessage", "Mock Test created and published successfully!");
        return "redirect:/admin/tests";
    }

    @PostMapping("/tests/{id}/delete")
    public String deleteMockTest(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteMockTest(id);
        redirectAttributes.addFlashAttribute("successMessage", "Mock Test deleted successfully.");
        return "redirect:/admin/tests";
    }

    // --- COMPANY MANAGEMENT ---
    @GetMapping("/companies")
    public String manageCompanies(Model model, Authentication authentication) {
        model.addAttribute("admin", getAuthenticatedAdmin(authentication));
        model.addAttribute("companies", eligibilityService.getAllCompanies());
        model.addAttribute("newCompany", new Company());
        return "admin/companies";
    }

    @PostMapping("/companies/add")
    public String addCompany(@ModelAttribute("newCompany") Company company, RedirectAttributes redirectAttributes) {
        adminService.saveCompany(company);
        redirectAttributes.addFlashAttribute("successMessage", "Company hiring criteria published successfully!");
        return "redirect:/admin/companies";
    }

    @PostMapping("/companies/{id}/delete")
    public String deleteCompany(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteCompany(id);
        redirectAttributes.addFlashAttribute("successMessage", "Company record removed successfully.");
        return "redirect:/admin/companies";
    }
}
