package com.placementportal.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.placementportal.entity.Resume;
import com.placementportal.entity.User;
import com.placementportal.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

/**
 * Service managing Resume creation, persistence, and OpenPDF ATS-compliant PDF generation.
 */
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    // Palette Colors
    private static final Color PRIMARY_NAVY = new Color(30, 58, 138); // #1E3A8A
    private static final Color TEXT_DARK = new Color(30, 41, 59);     // #1E293B
    private static final Color TEXT_MUTED = new Color(100, 116, 139); // #64748B
    private static final Color BORDER_LIGHT = new Color(226, 232, 240); // #E2E8F0

    @Autowired
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Optional<Resume> getResumeByStudent(User student) {
        return resumeRepository.findByStudent(student);
    }

    /**
     * Initializes default resume from student's profile if none exists yet.
     */
    public Resume getOrCreateResume(User student) {
        return resumeRepository.findByStudent(student).orElseGet(() -> {
            Resume r = new Resume();
            r.setStudent(student);
            r.setFullName(student.getFullName());
            r.setEmail(student.getEmail());
            r.setPhone(student.getPhone() != null ? student.getPhone() : "+91 9876543210");
            r.setLocation("Bangalore, India");
            r.setDegree(student.getDegree() != null ? student.getDegree() : "B.Tech");
            r.setBranch(student.getBranch() != null ? student.getBranch() : "Computer Science & Engineering");
            r.setCollege(student.getCollege() != null ? student.getCollege() : "Institute of Technology");
            r.setCgpa(student.getCgpa() != null ? student.getCgpa() : 8.2);
            r.setPassingYear(student.getPassingYear() != null ? student.getPassingYear() : 2026);
            r.setTwelfthSchool("State Senior Secondary School");
            r.setTwelfthPercentage(student.getTwelfthPercentage() != null ? student.getTwelfthPercentage() : 84.5);
            r.setTwelfthYear(2022);
            r.setTenthSchool("City High School");
            r.setTenthPercentage(student.getTenthPercentage() != null ? student.getTenthPercentage() : 88.0);
            r.setTenthYear(2020);
            r.setTechnicalSkills(student.getSkills() != null && !student.getSkills().isEmpty()
                    ? student.getSkills()
                    : "Java 21, Spring Boot, Spring Data JPA, MySQL, RESTful APIs, Git, HTML/CSS, JavaScript");
            r.setSoftSkills("Problem Solving, Team Collaboration, Analytical Thinking, Fast Learner");
            r.setToolsAndDatabases("IntelliJ IDEA, VS Code, Git, GitHub, MySQL Workbench, Postman");
            r.setSummary("Goal-oriented Computer Science Engineering student with strong fundamentals in Java, Object-Oriented Programming, and Full-Stack Web Development. Looking forward to contributing effectively as a Software Engineer.");
            
            r.setProject1Title("Placement Preparation Portal");
            r.setProject1Tech("Java, Spring Boot, Thymeleaf, MySQL, Chart.js");
            r.setProject1Desc("Engineered a full-stack student portal with timed mock tests, aptitude practice, safe Java code runner, and real-time eligibility checks.");
            r.setProject1Link("https://github.com/placement-portal");

            r.setProject2Title("Campus Event Management System");
            r.setProject2Tech("Java, Spring Boot, Hibernate, Bootstrap");
            r.setProject2Desc("Developed role-based college portal for hosting technical symposiums, ticketing, and student attendance tracking.");
            r.setProject2Link("https://github.com/campus-event-app");

            r.setExpRole("Software Intern");
            r.setExpCompany("Tech Solutions Inc.");
            r.setExpDuration("June 2024 - August 2024");
            r.setExpDesc("Assisted in REST API refactoring, unit test writing using JUnit, and database query optimization for core services.");

            r.setCertifications("Oracle Certified Associate Java SE, HackerRank 5-Star Problem Solving, Coursera Spring Framework");
            r.setAchievements("Finalist in State-Level Smart India Hackathon 2024; Top 5% in College Coding Contest.");

            return resumeRepository.save(r);
        });
    }

    @Transactional
    public Resume saveOrUpdateResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    /**
     * Generates a sleek, ATS-compliant PDF resume using OpenPDF.
     */
    public byte[] generatePdfResume(Resume resume) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        // Standard Fonts
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_NAVY);
        Font sectionHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, PRIMARY_NAVY);
        Font itemTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, TEXT_DARK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9.5f, TEXT_DARK);
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 8.5f, TEXT_MUTED);
        Font boldBodyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, TEXT_DARK);

        // Header: Name
        Paragraph namePara = new Paragraph(resume.getFullName().toUpperCase(), nameFont);
        namePara.setAlignment(Element.ALIGN_CENTER);
        document.add(namePara);

        // Header: Contact Details
        StringBuilder contactStr = new StringBuilder();
        if (isNotEmpty(resume.getEmail())) contactStr.append(resume.getEmail());
        if (isNotEmpty(resume.getPhone())) contactStr.append("  |  ").append(resume.getPhone());
        if (isNotEmpty(resume.getLocation())) contactStr.append("  |  ").append(resume.getLocation());
        if (isNotEmpty(resume.getLinkedinUrl())) contactStr.append("  |  LinkedIn: ").append(resume.getLinkedinUrl());
        if (isNotEmpty(resume.getGithubUrl())) contactStr.append("  |  GitHub: ").append(resume.getGithubUrl());

        Paragraph contactPara = new Paragraph(contactStr.toString(), subFont);
        contactPara.setAlignment(Element.ALIGN_CENTER);
        contactPara.setSpacingAfter(6f);
        document.add(contactPara);

        // Professional Summary
        if (isNotEmpty(resume.getSummary())) {
            addSectionHeading(document, "PROFESSIONAL SUMMARY", sectionHeaderFont);
            Paragraph sumPara = new Paragraph(resume.getSummary(), bodyFont);
            sumPara.setSpacingAfter(8f);
            document.add(sumPara);
        }

        // Education
        addSectionHeading(document, "EDUCATION", sectionHeaderFont);
        PdfPTable eduTable = new PdfPTable(new float[]{3.5f, 1.5f});
        eduTable.setWidthPercentage(100);
        eduTable.setSpacingAfter(8f);

        // College
        PdfPCell c1 = new PdfPCell();
        c1.setBorder(Rectangle.NO_BORDER);
        c1.addElement(new Paragraph(resume.getDegree() + " in " + resume.getBranch(), itemTitleFont));
        c1.addElement(new Paragraph(resume.getCollege(), bodyFont));
        eduTable.addCell(c1);

        PdfPCell c2 = new PdfPCell();
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph cgpaP = new Paragraph("CGPA: " + (resume.getCgpa() != null ? resume.getCgpa() : "N/A"), itemTitleFont);
        cgpaP.setAlignment(Element.ALIGN_RIGHT);
        Paragraph yearP = new Paragraph("Batch: " + (resume.getPassingYear() != null ? resume.getPassingYear() : "N/A"), subFont);
        yearP.setAlignment(Element.ALIGN_RIGHT);
        c2.addElement(cgpaP);
        c2.addElement(yearP);
        eduTable.addCell(c2);

        // Class 12 & 10
        if (isNotEmpty(resume.getTwelfthSchool())) {
            PdfPCell c3 = new PdfPCell(new Paragraph("Class XII (Higher Secondary) - " + resume.getTwelfthSchool(), bodyFont));
            c3.setBorder(Rectangle.NO_BORDER);
            eduTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Paragraph((resume.getTwelfthPercentage() != null ? resume.getTwelfthPercentage() + "%" : "") + " | " + (resume.getTwelfthYear() != null ? resume.getTwelfthYear() : ""), subFont));
            c4.setBorder(Rectangle.NO_BORDER);
            c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            eduTable.addCell(c4);
        }

        if (isNotEmpty(resume.getTenthSchool())) {
            PdfPCell c5 = new PdfPCell(new Paragraph("Class X (Secondary) - " + resume.getTenthSchool(), bodyFont));
            c5.setBorder(Rectangle.NO_BORDER);
            eduTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Paragraph((resume.getTenthPercentage() != null ? resume.getTenthPercentage() + "%" : "") + " | " + (resume.getTenthYear() != null ? resume.getTenthYear() : ""), subFont));
            c6.setBorder(Rectangle.NO_BORDER);
            c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
            eduTable.addCell(c6);
        }
        document.add(eduTable);

        // Technical Skills
        addSectionHeading(document, "TECHNICAL SKILLS", sectionHeaderFont);
        Paragraph skillPara = new Paragraph();
        skillPara.setSpacingAfter(8f);
        if (isNotEmpty(resume.getTechnicalSkills())) {
            skillPara.add(new Chunk("Core Skills & Languages: ", boldBodyFont));
            skillPara.add(new Chunk(resume.getTechnicalSkills() + "\n", bodyFont));
        }
        if (isNotEmpty(resume.getToolsAndDatabases())) {
            skillPara.add(new Chunk("Databases & Tools: ", boldBodyFont));
            skillPara.add(new Chunk(resume.getToolsAndDatabases() + "\n", bodyFont));
        }
        if (isNotEmpty(resume.getSoftSkills())) {
            skillPara.add(new Chunk("Professional Skills: ", boldBodyFont));
            skillPara.add(new Chunk(resume.getSoftSkills(), bodyFont));
        }
        document.add(skillPara);

        // Academic Projects
        addSectionHeading(document, "KEY PROJECTS", sectionHeaderFont);
        if (isNotEmpty(resume.getProject1Title())) {
            Paragraph p1Head = new Paragraph();
            p1Head.add(new Chunk(resume.getProject1Title(), itemTitleFont));
            if (isNotEmpty(resume.getProject1Tech())) {
                p1Head.add(new Chunk(" | " + resume.getProject1Tech(), subFont));
            }
            document.add(p1Head);

            if (isNotEmpty(resume.getProject1Desc())) {
                Paragraph p1Desc = new Paragraph("• " + resume.getProject1Desc(), bodyFont);
                if (isNotEmpty(resume.getProject1Link())) {
                    p1Desc.add(new Chunk(" (Link: " + resume.getProject1Link() + ")", subFont));
                }
                p1Desc.setSpacingAfter(4f);
                document.add(p1Desc);
            }
        }

        if (isNotEmpty(resume.getProject2Title())) {
            Paragraph p2Head = new Paragraph();
            p2Head.add(new Chunk(resume.getProject2Title(), itemTitleFont));
            if (isNotEmpty(resume.getProject2Tech())) {
                p2Head.add(new Chunk(" | " + resume.getProject2Tech(), subFont));
            }
            document.add(p2Head);

            if (isNotEmpty(resume.getProject2Desc())) {
                Paragraph p2Desc = new Paragraph("• " + resume.getProject2Desc(), bodyFont);
                if (isNotEmpty(resume.getProject2Link())) {
                    p2Desc.add(new Chunk(" (Link: " + resume.getProject2Link() + ")", subFont));
                }
                p2Desc.setSpacingAfter(6f);
                document.add(p2Desc);
            }
        }

        // Experience / Internship (if present)
        if (isNotEmpty(resume.getExpRole())) {
            addSectionHeading(document, "EXPERIENCE & INTERNSHIPS", sectionHeaderFont);
            Paragraph expHead = new Paragraph();
            expHead.add(new Chunk(resume.getExpRole() + " - " + resume.getExpCompany(), itemTitleFont));
            if (isNotEmpty(resume.getExpDuration())) {
                expHead.add(new Chunk(" (" + resume.getExpDuration() + ")", subFont));
            }
            document.add(expHead);
            if (isNotEmpty(resume.getExpDesc())) {
                Paragraph expD = new Paragraph("• " + resume.getExpDesc(), bodyFont);
                expD.setSpacingAfter(6f);
                document.add(expD);
            }
        }

        // Certifications & Achievements
        if (isNotEmpty(resume.getCertifications()) || isNotEmpty(resume.getAchievements())) {
            addSectionHeading(document, "CERTIFICATIONS & ACHIEVEMENTS", sectionHeaderFont);
            Paragraph certPara = new Paragraph();
            if (isNotEmpty(resume.getCertifications())) {
                certPara.add(new Chunk("Certifications: ", boldBodyFont));
                certPara.add(new Chunk(resume.getCertifications() + "\n", bodyFont));
            }
            if (isNotEmpty(resume.getAchievements())) {
                certPara.add(new Chunk("Achievements: ", boldBodyFont));
                certPara.add(new Chunk(resume.getAchievements(), bodyFont));
            }
            certPara.setSpacingAfter(6f);
            document.add(certPara);
        }

        document.close();
        return out.toByteArray();
    }

    private void addSectionHeading(Document doc, String title, Font font) throws DocumentException {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(4f);
        p.setSpacingAfter(2f);
        doc.add(p);

        LineSeparator ls = new LineSeparator(1f, 100, BORDER_LIGHT, Element.ALIGN_CENTER, -2);
        doc.add(ls);
        doc.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 3)));
    }

    private boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
