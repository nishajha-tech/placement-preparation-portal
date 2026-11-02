# Placement Preparation Portal 🎓
> **Full-Stack Java College Project | Spring Boot 3, Spring Data JPA, MySQL/H2, Thymeleaf, Security & Chart.js**

A complete, beginner-friendly web application designed for B.Tech / B.E. / MCA students to prepare for on-campus and off-campus placements. The portal provides aptitude practice, timed mock assessments, safe Java DSA code evaluation, an ATS-friendly resume builder with PDF export, real-time company eligibility checking, progress analytics, and an administrative control panel.

---

## 🚀 Key Features

1. **Student Registration & Authentication**:
   - Secure BCrypt password hashing.
   - Role-Based Access Control (`ROLE_STUDENT`, `ROLE_ADMIN`).
   - Profile management with CGPA, 10th%, 12th%, active backlogs, passing year, and technical skills.

2. **Student Dashboard & Analytics**:
   - Placement Readiness Index Score (0–100%) calculated from academics, mock tests, and coding challenges.
   - Quick statistics cards: Tests attempted, average score, aptitude practice count, coding solved count, and eligible companies.
   - Recent mock test history with performance status badges.

3. **Aptitude Practice Bank**:
   - Organized across **Quantitative**, **Logical Reasoning**, **Verbal Ability**, and **Technical & Java** categories.
   - Filter by Difficulty (Easy, Medium, Hard).
   - Instant "Check Answer" verification with detailed step-by-step mathematical formulas and logical reasoning.

4. **Timed Mock Tests**:
   - Company-specific assessments simulating **TCS NQT**, **Infosys**, and **Wipro** test environments.
   - Live JavaScript countdown timer with automatic submission on timeout.
   - Interactive Question Palette with Answered, Unanswered, Marked for Review, and Current indicators.
   - Comprehensive Scorecard with percentage, time taken, pass/fail badge, and question-by-question review.

5. **Java Coding Practice Sandbox**:
   - Popular placement DSA problems (*Two Sum*, *Reverse String*, *Valid Palindrome*, *Missing Number*).
   - Built-in Java 21 compilation and sandboxed test-case evaluation.
   - Real-time test-case pass/fail breakdown with expected vs actual output comparison and execution timeout protection.

6. **ATS Resume Builder with PDF Download**:
   - Structured resume form capturing Contact details, Career Objective, Education, Technical Skills, Projects, and Certifications.
   - Live ATS format preview side-by-side with editor.
   - Server-side PDF generation using **OpenPDF** for instant, high-resolution downloadable PDF files.

7. **Placement Eligibility Checker & "What-If" Simulator**:
   - Rule engine evaluating student eligibility against top tech recruiters (TCS, Infosys, Amazon, Microsoft, Wipro, Cognizant, Google).
   - Visual checklist highlighting passed criteria and exact reasons for criteria not met.
   - Interactive **"What-If" Academic Simulator** allowing students to test hypothetical CGPA or backlog changes to see unlocked opportunities.

8. **Progress Analytics with Chart.js**:
   - Category-wise Aptitude Accuracy Radar/Bar Chart.
   - Mock Test Score Progression Line Chart.
   - DSA Coding Problems Solved by Difficulty Doughnut Chart.
   - AI-style recommendations highlighting strengths, weaknesses, and next preparation steps.

9. **Admin Management Panel**:
   - System-wide dashboard statistics.
   - Student roster management (view credentials, academic parameters, remove records).
   - Question Bank management (add, edit, filter, delete questions).
   - Mock Test creator (configure duration, marks, link questions from bank).
   - Company hiring criteria manager (add packages, cutoffs, deadlines).

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Backend Framework** | Java 21, Spring Boot 3.3.5 |
| **Architecture** | Spring MVC (Model-View-Controller) |
| **Data Persistence** | Spring Data JPA, Hibernate ORM |
| **Databases** | MySQL 8.0 (Production) & H2 (Instant Zero-Config Demo Mode) |
| **Security** | Spring Security 6 with BCryptPasswordEncoder |
| **Frontend Templates** | Thymeleaf 3 with Spring Security extras |
| **Styling & UI** | Vanilla CSS3 (Custom Glassmorphism Design System) |
| **Interactivity** | Vanilla JavaScript (ES6+), Fetch API |
| **Data Visualization** | Chart.js 4.4 |
| **Document Generation** | OpenPDF 1.3.40 (LibrePDF) |
| **Build Tool** | Apache Maven 3.9+ |

---

## 📂 Project Directory Structure

```text
PlacementPreparationPortal/
├── pom.xml
├── run-h2.bat               <-- 1-Click Instant Demo (Zero Setup)
├── run-mysql.bat            <-- 1-Click MySQL Server Mode
├── src/
│   ├── main/
│   │   ├── java/com/placementportal/
│   │   │   ├── PlacementPortalApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── AptitudeController.java
│   │   │   │   ├── MockTestController.java
│   │   │   │   ├── CodingController.java
│   │   │   │   ├── ResumeController.java
│   │   │   │   ├── EligibilityController.java
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Question.java
│   │   │   │   ├── MockTest.java
│   │   │   │   ├── TestAttempt.java
│   │   │   │   ├── CodingProblem.java
│   │   │   │   ├── CodeSubmission.java
│   │   │   │   ├── Company.java
│   │   │   │   ├── Resume.java
│   │   │   │   └── PracticeHistory.java
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── dto/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-h2.properties
│   │       ├── application-mysql.properties
│   │       ├── schema-mysql.sql
│   │       ├── static/
│   │       │   ├── css/main.css
│   │       │   └── js/app.js
│   │       └── templates/
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── dashboard.html
│   │           ├── profile.html
│   │           ├── aptitude.html
│   │           ├── mock-tests.html
│   │           ├── test-instructions.html
│   │           ├── test-environment.html
│   │           ├── test-result.html
│   │           ├── test-history.html
│   │           ├── coding-list.html
│   │           ├── coding-editor.html
│   │           ├── resume-builder.html
│   │           ├── eligibility.html
│   │           ├── analytics.html
│   │           ├── fragments/
│   │           └── admin/
```

---

## ⚡ Quick Start & Running Instructions

### Option 1: Instant Zero-Config Mode (Recommended for College Viva Demo)
Double click **`run-h2.bat`** or run:
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```
- Starts immediately on port **8088**.
- Automatically seeds demo students, questions, mock tests, and companies.
- Access Portal: [http://localhost:8088](http://localhost:8088)
- H2 Web Console: [http://localhost:8088/h2-console](http://localhost:8088/h2-console) (JDBC URL: `jdbc:h2:mem:placementdb`, Username: `sa`, Password: *(leave blank)*)

### Option 2: MySQL Server Mode
1. Ensure your local MySQL service is running.
2. In `application-mysql.properties`, verify your MySQL username and password (defaults to `root`/`root`).
3. Double click **`run-mysql.bat`** or run:
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

---

## 🔑 Pre-Seeded Demo Login Credentials

| Role | Email | Password | Details |
|---|---|---|---|
| **Student (High CGPA)** | `rahul@student.com` | `student123` | B.Tech CSE, 8.4 CGPA, 0 Backlogs, Eligible for Amazon, TCS, etc. |
| **Student (1 Backlog)** | `sneha@student.com` | `student123` | B.Tech IT, 7.2 CGPA, 1 Backlog, Demonstrates backlog restrictions |
| **TPO Administrator** | `admin@portal.com` | `admin123` | Full access to manage questions, tests, companies, and students |

---

## 🎓 College Viva & Interview Preparation (Top 15 Q&A)

1. **Q: What is Spring Boot and why did you choose it over standard Spring Framework?**
   - **A:** Spring Boot reduces boilerplate XML configuration through auto-configuration, provides an embedded web container (Tomcat), and bundles starter dependencies (like `spring-boot-starter-web`), allowing faster application development.

2. **Q: How is security handled in this project?**
   - **A:** We use **Spring Security 6** with `BCryptPasswordEncoder`. Passwords are never saved in plain text; they are one-way hashed with salted BCrypt. Authorization is enforced using `SecurityFilterChain` with role checks (`hasRole('ADMIN')` vs `hasAnyRole('STUDENT', 'ADMIN')`).

3. **Q: How does the Placement Eligibility Checker evaluate students?**
   - **A:** In `EligibilityService`, the student's profile attributes (`cgpa`, `tenthPercentage`, `twelfthPercentage`, `activeBacklogs`, `branch`) are compared against each company's threshold criteria (`minCgpa`, `maxBacklogs`, etc.). It produces passed and failed reasoning strings for transparent student feedback.

4. **Q: How does the safe Java coding sandbox execute code?**
   - **A:** In `CodingService`, code is first validated against malicious APIs (`ProcessBuilder`, `Runtime.getRuntime`, file deletion). Then, the Java Compiler API (`javax.tools.JavaCompiler`) compiles the solution. An `ExecutorService` runs the test cases with a strict **2-second timeout** to protect against infinite loops (`while(true)`).

5. **Q: How is the PDF resume generated?**
   - **A:** We use **OpenPDF** (`com.github.librepdf:openpdf`). When the student requests `/resume/download`, `ResumeService.generatePdfResume()` constructs a formatted ATS-compliant document in memory (`ByteArrayOutputStream`) and returns it with HTTP header `Content-Disposition: attachment`.

6. **Q: What is Spring Data JPA and how does it simplify database queries?**
   - **A:** Spring Data JPA provides repository interfaces (`JpaRepository`) that automatically generate SQL implementation at runtime for common CRUD operations and finder queries (e.g. `findByEmail`, `findByCategory`).

7. **Q: How do you switch between MySQL and H2 database?**
   - **A:** We use Spring Profiles (`@Profile("h2")` or `@Profile("mysql")`). The configuration is separated into `application-h2.properties` and `application-mysql.properties`, and activated using `spring.profiles.active`.
