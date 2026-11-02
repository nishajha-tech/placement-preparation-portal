package com.placementportal.controller;

import com.placementportal.entity.Resume;
import com.placementportal.entity.User;
import com.placementportal.service.ResumeService;
import com.placementportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling the Student Resume Builder, real-time preview, and PDF export.
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @Autowired
    public ResumeController(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public String resumeEditor(Model model, Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        Resume resume = resumeService.getOrCreateResume(student);

        model.addAttribute("student", student);
        model.addAttribute("resume", resume);

        return "resume-builder";
    }

    @PostMapping("/save")
    public String saveResume(@ModelAttribute("resume") Resume updatedResume,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) return "redirect:/login";

        Resume existing = resumeService.getOrCreateResume(student);
        updatedResume.setId(existing.getId());
        updatedResume.setStudent(student);

        resumeService.saveOrUpdateResume(updatedResume);
        redirectAttributes.addFlashAttribute("successMessage", "Resume updated successfully!");

        return "redirect:/resume";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdfResume(Authentication authentication) {
        User student = getAuthenticatedUser(authentication);
        if (student == null) {
            return ResponseEntity.status(401).build();
        }

        Resume resume = resumeService.getOrCreateResume(student);
        try {
            byte[] pdfBytes = resumeService.generatePdfResume(resume);

            String safeName = (resume.getFullName() != null ? resume.getFullName().replaceAll("[^a-zA-Z0-9]", "_") : "Student");
            String filename = safeName + "_Placement_Resume.pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
