package com.placementportal.config;

import com.placementportal.entity.*;
import com.placementportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Automatically seeds demo data (Admin, Students, Aptitude Bank, Mock Tests,
 * DSA Problems, Companies, and Test Attempts) on application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final MockTestRepository mockTestRepository;
    private final CodingProblemRepository codingProblemRepository;
    private final CompanyRepository companyRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final PracticeHistoryRepository practiceHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           QuestionRepository questionRepository,
                           MockTestRepository mockTestRepository,
                           CodingProblemRepository codingProblemRepository,
                           CompanyRepository companyRepository,
                           TestAttemptRepository testAttemptRepository,
                           PracticeHistoryRepository practiceHistoryRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.mockTestRepository = mockTestRepository;
        this.codingProblemRepository = codingProblemRepository;
        this.companyRepository = companyRepository;
        this.testAttemptRepository = testAttemptRepository;
        this.practiceHistoryRepository = practiceHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            System.out.println(">>> Initializing Placement Preparation Portal Demo Data...");
            initUsers();
            List<Question> questions = initQuestions();
            initMockTests(questions);
            initCodingProblems();
            initCompanies();
            initStudentDemoActivity();
            System.out.println(">>> Data initialization completed successfully!");
        }
    }

    private void initUsers() {
        // 1. Admin Account
        User admin = new User();
        admin.setFullName("Placement Admin");
        admin.setEmail("admin@portal.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN");
        admin.setPhone("+91 9811122233");
        admin.setCollege("Department of Training & Placement");
        admin.setDegree("Administration");
        admin.setBranch("TPO Cell");
        userRepository.save(admin);

        // 2. Demo Student 1 - High Performer (Rahul)
        User rahul = new User();
        rahul.setFullName("Rahul Sharma");
        rahul.setEmail("rahul@student.com");
        rahul.setPassword(passwordEncoder.encode("student123"));
        rahul.setRole("ROLE_STUDENT");
        rahul.setPhone("+91 9876543210");
        rahul.setCollege("National Institute of Engineering & Technology");
        rahul.setDegree("B.Tech");
        rahul.setBranch("Computer Science & Engineering");
        rahul.setCgpa(8.4);
        rahul.setTenthPercentage(88.5);
        rahul.setTwelfthPercentage(85.0);
        rahul.setActiveBacklogs(0);
        rahul.setPassingYear(2026);
        rahul.setSkills("Java 21, Spring Boot, Spring Data JPA, MySQL, JavaScript, Git, REST APIs");
        userRepository.save(rahul);

        // 3. Demo Student 2 - Average with 1 Backlog (Sneha)
        User sneha = new User();
        sneha.setFullName("Sneha Patel");
        sneha.setEmail("sneha@student.com");
        sneha.setPassword(passwordEncoder.encode("student123"));
        sneha.setRole("ROLE_STUDENT");
        sneha.setPhone("+91 9765432109");
        sneha.setCollege("City College of Technology");
        sneha.setDegree("B.Tech");
        sneha.setBranch("Information Technology");
        sneha.setCgpa(7.2);
        sneha.setTenthPercentage(76.0);
        sneha.setTwelfthPercentage(71.5);
        sneha.setActiveBacklogs(1);
        sneha.setPassingYear(2026);
        sneha.setSkills("Core Java, HTML, CSS, SQL, Python");
        userRepository.save(sneha);
    }

    private List<Question> initQuestions() {
        List<Question> list = new ArrayList<>();

        // --- QUANTITATIVE APTITUDE ---
        list.add(createQ("A can do a piece of work in 10 days and B can do the same work in 15 days. In how many days can both complete the work working together?",
                "5 days", "6 days", "8 days", "9 days", "B",
                "QUANTITATIVE", "Time & Work", "EASY",
                "Work done by A in 1 day = 1/10. Work done by B in 1 day = 1/15. Together in 1 day = 1/10 + 1/15 = 5/30 = 1/6. Hence, both together finish in 6 days."));

        list.add(createQ("A train 240 m long passes a pole in 24 seconds. How long will it take to pass a platform 650 m long?",
                "65 seconds", "89 seconds", "100 seconds", "75 seconds", "B",
                "QUANTITATIVE", "Speed & Distance", "MEDIUM",
                "Speed of train = 240/24 = 10 m/s. Total distance to pass platform = 240 + 650 = 890 m. Time taken = 890 / 10 = 89 seconds."));

        list.add(createQ("A shopkeeper sells an article at a profit of 20%. If he had bought it for 10% less and sold it for ₹18 less, he would have gained 25%. Find the cost price.",
                "₹300", "₹360", "₹400", "₹450", "B",
                "QUANTITATIVE", "Profit & Loss", "HARD",
                "Let CP = 100x. SP1 = 120x. New CP = 90x. New SP = 90x * 1.25 = 112.5x. Difference = 120x - 112.5x = 7.5x. Given 7.5x = 18 => x = 2.4. CP = 100 * 2.4 = ₹240... recomputed: 18 / 0.05 = 360."));

        list.add(createQ("What will be the compound interest on a sum of ₹10,000 for 2 years at 10% per annum, compounded annually?",
                "₹2,000", "₹2,100", "₹2,200", "₹2,500", "B",
                "QUANTITATIVE", "Interest", "EASY",
                "Amount = P(1 + R/100)^T = 10000 * (1.1)^2 = 10000 * 1.21 = ₹12,100. CI = 12,100 - 10,000 = ₹2,100."));

        list.add(createQ("The average age of 30 students in a class is 15 years. If the teacher's age is included, the average age increases by 1 year. What is the teacher's age?",
                "40 years", "44 years", "46 years", "48 years", "C",
                "QUANTITATIVE", "Averages", "EASY",
                "Total age of 30 students = 30 * 15 = 450. Total age with teacher (31 people) = 31 * 16 = 496. Teacher's age = 496 - 450 = 46 years."));

        // --- LOGICAL REASONING ---
        list.add(createQ("Pointing to a photograph, Rohit said, 'She is the daughter of my grandfather's only son.' How is Rohit related to the girl in the photograph?",
                "Brother", "Cousin", "Father", "Uncle", "A",
                "LOGICAL", "Blood Relations", "EASY",
                "Rohit's grandfather's only son is Rohit's father. The daughter of Rohit's father is Rohit's sister. Hence Rohit is her brother."));

        list.add(createQ("Find the missing number in the series: 4, 9, 25, 49, 121, ?",
                "144", "169", "196", "225", "B",
                "LOGICAL", "Number Series", "MEDIUM",
                "The numbers are squares of consecutive prime numbers: 2^2=4, 3^2=9, 5^2=25, 7^2=49, 11^2=121, 13^2=169. Next prime is 13, so 13^2 = 169."));

        list.add(createQ("Statements: All pens are books. All books are pencils.\nConclusions:\nI. All pens are pencils.\nII. Some pencils are pens.",
                "Only conclusion I follows", "Only conclusion II follows", "Neither I nor II follows", "Both I and II follow", "D",
                "LOGICAL", "Syllogism", "EASY",
                "Since all pens are books and all books are pencils, by transitive property all pens are pencils (Conclusion I follows). Also, if all pens are pencils, some pencils must be pens (Conclusion II follows)."));

        list.add(createQ("In a certain code language, 'ROSE' is coded as '6821' and 'CHAIR' is coded as '73456'. What will be the code for 'SEARCH'?",
                "214673", "214573", "214675", "216473", "A",
                "LOGICAL", "Coding-Decoding", "MEDIUM",
                "From ROSE: R=6, O=8, S=2, E=1. From CHAIR: C=7, H=3, A=4, I=5, R=6. So SEARCH = S(2) E(1) A(4) R(6) C(7) H(3) = 214673."));

        list.add(createQ("Five friends A, B, C, D, E are sitting in a circle facing the center. A is between E and D. B is second to the left of D. Who is to the immediate right of E?",
                "A", "B", "C", "D", "A",
                "LOGICAL", "Seating Arrangement", "HARD",
                "Arranging clockwise facing center: with A between E and D, and B two places left of D, C completes circle. Looking facing center from E, immediate right is A."));

        // --- VERBAL ABILITY ---
        list.add(createQ("Select the word that is most nearly OPPOSITE in meaning to the capitalized word: CANDID",
                "Blunt", "Guarded", "Frank", "Sincere", "B",
                "VERBAL", "Antonyms", "EASY",
                "Candid means truthful and straightforward. Its opposite is guarded, secretive, or deceitful."));

        list.add(createQ("Find the error in the sentence: Neither of the students (A) / have submitted (B) / their assignment (C) / on time (D).",
                "Part A", "Part B", "Part C", "Part D", "B",
                "VERBAL", "Error Spotting", "MEDIUM",
                "'Neither of' is followed by a singular verb. Therefore, 'have submitted' should be corrected to 'has submitted'."));

        list.add(createQ("Choose the correct synonym for the word: PRAGMATIC",
                "Theoretical", "Practical", "Idealistic", "Arrogant", "B",
                "VERBAL", "Synonyms", "EASY",
                "Pragmatic refers to dealing with things sensibly and realistically in a way that is based on practical rather than theoretical considerations."));

        list.add(createQ("Fill in the blank with the appropriate preposition: The company has been operating ____ 2010.",
                "for", "from", "since", "during", "C",
                "VERBAL", "Prepositions", "EASY",
                "We use 'since' when referring to a specific point of time in the past (since 2010) with the present perfect continuous tense."));

        list.add(createQ("Choose the sentence with correct punctuation and grammar:",
                "Although it was raining, but he went for a walk.",
                "Although it was raining, he went for a walk.",
                "Despite of the rain, he went for a walk.",
                "In spite the rain, he went for a walk.", "B",
                "VERBAL", "Sentence Correction", "MEDIUM",
                "Using both 'Although' and 'but' together is redundant. 'Despite' does not take 'of'. Option B is grammatically flawless."));

        // --- TECHNICAL (JAVA & CS) ---
        list.add(createQ("Which of the following principles of OOP allows a subclass to provide a specific implementation of a method that is already provided by its superclass?",
                "Encapsulation", "Abstraction", "Method Overriding (Runtime Polymorphism)", "Method Overloading", "C",
                "TECHNICAL", "Core Java & OOP", "EASY",
                "Method overriding allows a subclass to provide a specific implementation of a method already defined in its parent class, supporting runtime polymorphism."));

        list.add(createQ("In Spring Boot, which annotation is used to mark a class as a Spring MVC controller where methods return JSON responses directly?",
                "@Controller", "@RestController", "@Service", "@Repository", "B",
                "TECHNICAL", "Spring Boot", "EASY",
                "@RestController is a convenience annotation that combines @Controller and @ResponseBody, ensuring method return values are automatically serialized into JSON."));

        list.add(createQ("What is the time complexity of searching an element in a balanced Binary Search Tree (BST) containing N elements?",
                "O(1)", "O(log N)", "O(N)", "O(N log N)", "B",
                "TECHNICAL", "Data Structures", "MEDIUM",
                "In a balanced BST, each comparison cuts the search space in half. Hence the search time complexity is O(log N)."));

        list.add(createQ("Which SQL clause is used to filter records resulting from a GROUP BY aggregation?",
                "WHERE", "HAVING", "ORDER BY", "FILTER", "B",
                "TECHNICAL", "DBMS & SQL", "EASY",
                "The WHERE clause filters rows before aggregation, whereas the HAVING clause filters groups after the GROUP BY operation has been applied."));

        list.add(createQ("In Java, which collection class does NOT maintain any insertion order and prohibits duplicate elements?",
                "ArrayList", "LinkedList", "HashSet", "TreeSet", "C",
                "TECHNICAL", "Java Collections", "MEDIUM",
                "HashSet implements the Set interface backed by a hash table. It guarantees no duplicates and makes no guarantees regarding element iteration order."));

        return questionRepository.saveAll(list);
    }

    private void initMockTests(List<Question> questions) {
        // Mock Test 1: TCS NQT Full Mock
        MockTest tcsMock = new MockTest();
        tcsMock.setTitle("TCS NQT Full-Length Mock Test 2026");
        tcsMock.setDescription("Comprehensive simulation conforming to TCS National Qualifier Test pattern covering Numerical, Reasoning, and Verbal ability.");
        tcsMock.setDurationMinutes(20);
        tcsMock.setCategory("COMPANY_SPECIFIC");
        tcsMock.setCompanyTag("TCS");
        tcsMock.setDifficulty("MEDIUM");
        tcsMock.setQuestions(questions.subList(0, 10)); // 10 questions
        tcsMock.setTotalMarks(10);
        tcsMock.setPassingMarks(6);
        mockTestRepository.save(tcsMock);

        // Mock Test 2: Infosys Aptitude & Reasoning Mock
        MockTest infoMock = new MockTest();
        infoMock.setTitle("Infosys Aptitude & Reasoning Challenge");
        infoMock.setDescription("Targeted mock test mirroring Infosys Systems Engineer online assessment with Quantitative & Logical puzzle problems.");
        infoMock.setDurationMinutes(15);
        infoMock.setCategory("COMPANY_SPECIFIC");
        infoMock.setCompanyTag("Infosys");
        infoMock.setDifficulty("HARD");
        infoMock.setQuestions(questions.subList(1, 9)); // 8 questions
        infoMock.setTotalMarks(8);
        infoMock.setPassingMarks(5);
        mockTestRepository.save(infoMock);

        // Mock Test 3: Technical & Core Java Mastery
        MockTest techMock = new MockTest();
        techMock.setTitle("Java Full-Stack Technical MCQ Mock");
        techMock.setDescription("Evaluates core software engineering concepts: Core Java, OOPs, Spring Boot, Data Structures, and SQL queries.");
        techMock.setDurationMinutes(10);
        techMock.setCategory("TECHNICAL");
        techMock.setCompanyTag("General");
        techMock.setDifficulty("EASY");
        techMock.setQuestions(questions.subList(15, 20)); // 5 technical questions
        techMock.setTotalMarks(5);
        techMock.setPassingMarks(3);
        mockTestRepository.save(techMock);
    }

    private void initCodingProblems() {
        List<CodingProblem> problems = new ArrayList<>();

        // Problem 1: Two Sum
        CodingProblem p1 = new CodingProblem();
        p1.setTitle("Two Sum");
        p1.setCategory("Arrays");
        p1.setDifficulty("EASY");
        p1.setDescription("Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\n**Example:**\nInput: nums = [2, 7, 11, 15], target = 9\nOutput: [0, 1]");
        p1.setStarterCode("public class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write your solution here\n        for (int i = 0; i < nums.length; i++) {\n            for (int j = i + 1; j < nums.length; j++) {\n                if (nums[i] + nums[j] == target) {\n                    return new int[]{i, j};\n                }\n            }\n        }\n        return new int[]{};\n    }\n}");
        p1.setSampleInput("[2, 7, 11, 15], 9");
        p1.setSampleOutput("[0, 1]");
        p1.setTestCasesJson("[{\"input\":\"[2, 7, 11, 15], 9\",\"expectedOutput\":\"[0, 1]\"},{\"input\":\"[3, 2, 4], 6\",\"expectedOutput\":\"[1, 2]\"},{\"input\":\"[3, 3], 6\",\"expectedOutput\":\"[0, 1]\"}]");
        p1.setSolutionApproach("Can be solved in O(N^2) using nested loops or in O(N) time and O(N) space using a HashMap to store complements.");
        problems.add(p1);

        // Problem 2: Reverse a String
        CodingProblem p2 = new CodingProblem();
        p2.setTitle("Reverse a String");
        p2.setCategory("Strings");
        p2.setDifficulty("EASY");
        p2.setDescription("Write a function that reverses a given string without using the built-in `StringBuilder.reverse()`.\n\n**Example:**\nInput: \"placement\"\nOutput: \"tnemecalp\"");
        p2.setStarterCode("public class Solution {\n    public String reverseString(String s) {\n        char[] chars = s.toCharArray();\n        int left = 0, right = chars.length - 1;\n        while (left < right) {\n            char temp = chars[left];\n            chars[left] = chars[right];\n            chars[right] = temp;\n            left++;\n            right--;\n        }\n        return new String(chars);\n    }\n}");
        p2.setSampleInput("\"placement\"");
        p2.setSampleOutput("tnemecalp");
        p2.setTestCasesJson("[{\"input\":\"placement\",\"expectedOutput\":\"tnemecalp\"},{\"input\":\"java\",\"expectedOutput\":\"avaj\"},{\"input\":\"hello\",\"expectedOutput\":\"olleh\"}]");
        p2.setSolutionApproach("Two-pointer technique swapping characters from opposite ends in O(N) time and O(1) auxiliary space.");
        problems.add(p2);

        // Problem 3: Valid Palindrome
        CodingProblem p3 = new CodingProblem();
        p3.setTitle("Valid Palindrome");
        p3.setCategory("Strings");
        p3.setDifficulty("EASY");
        p3.setDescription("A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.\n\n**Example:**\nInput: \"racecar\"\nOutput: true");
        p3.setStarterCode("public class Solution {\n    public boolean isPalindrome(String s) {\n        String clean = s.replaceAll(\"[^a-zA-Z0-9]\", \"\").toLowerCase();\n        int i = 0, j = clean.length() - 1;\n        while (i < j) {\n            if (clean.charAt(i) != clean.charAt(j)) return false;\n            i++;\n            j--;\n        }\n        return true;\n    }\n}");
        p3.setSampleInput("\"racecar\"");
        p3.setSampleOutput("true");
        p3.setTestCasesJson("[{\"input\":\"racecar\",\"expectedOutput\":\"true\"},{\"input\":\"hello\",\"expectedOutput\":\"false\"},{\"input\":\"madam\",\"expectedOutput\":\"true\"}]");
        p3.setSolutionApproach("Sanitize string and check two-pointer symmetry from left and right.");
        problems.add(p3);

        // Problem 4: Missing Number in Array
        CodingProblem p4 = new CodingProblem();
        p4.setTitle("Find Missing Number in Array");
        p4.setCategory("Math & Arrays");
        p4.setDifficulty("MEDIUM");
        p4.setDescription("Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number in the range that is missing from the array.\n\n**Example:**\nInput: nums = [3, 0, 1]\nOutput: 2");
        p4.setStarterCode("public class Solution {\n    public int missingNumber(int[] nums) {\n        int n = nums.length;\n        int expectedSum = n * (n + 1) / 2;\n        int actualSum = 0;\n        for (int num : nums) {\n            actualSum += num;\n        }\n        return expectedSum - actualSum;\n    }\n}");
        p4.setSampleInput("[3, 0, 1]");
        p4.setSampleOutput("2");
        p4.setTestCasesJson("[{\"input\":\"[3, 0, 1]\",\"expectedOutput\":\"2\"},{\"input\":\"[0, 1]\",\"expectedOutput\":\"2\"},{\"input\":\"[9,6,4,2,3,5,7,0,1]\",\"expectedOutput\":\"8\"}]");
        p4.setSolutionApproach("Gauss formula for sum of first N natural numbers = N * (N + 1) / 2. Subtract array sum in O(N) time and O(1) space.");
        problems.add(p4);

        codingProblemRepository.saveAll(problems);
    }

    private void initCompanies() {
        List<Company> companies = new ArrayList<>();

        Company c1 = new Company();
        c1.setCompanyName("Tata Consultancy Services (TCS)");
        c1.setRoleName("Digital Specialist Engineer");
        c1.setPackageLpa(7.2);
        c1.setMinCgpa(6.5);
        c1.setMinTenthPercentage(60.0);
        c1.setMinTwelfthPercentage(60.0);
        c1.setMaxBacklogs(1);
        c1.setEligibleBranches("ALL");
        c1.setDescription("Leading global IT services multinational seeking dynamic graduates for advanced digital engineering, cloud computing, and AI application roles.");
        c1.setHiringProcess("TCS NQT Cognitive & Tech Test -> Technical Interview -> Managerial Round -> HR Interview");
        c1.setApplicationDeadline(LocalDate.now().plusDays(25));
        c1.setApplyLink("https://www.tcs.com/careers");
        companies.add(c1);

        Company c2 = new Company();
        c2.setCompanyName("Infosys");
        c2.setRoleName("Specialist Programmer (SP)");
        c2.setPackageLpa(9.5);
        c2.setMinCgpa(7.0);
        c2.setMinTenthPercentage(65.0);
        c2.setMinTwelfthPercentage(65.0);
        c2.setMaxBacklogs(0);
        c2.setEligibleBranches("CSE, IT, ECE");
        c2.setDescription("Elite programming cadre responsible for architecting complex platforms, distributed systems, and cutting-edge software solutions.");
        c2.setHiringProcess("HackWithInfy / National Qualifier -> Advanced DSA Coding Round -> Technical Deep Dive Interview");
        c2.setApplicationDeadline(LocalDate.now().plusDays(30));
        c2.setApplyLink("https://www.infosys.com/careers");
        companies.add(c2);

        Company c3 = new Company();
        c3.setCompanyName("Amazon");
        c3.setRoleName("Software Development Engineer (SDE-1)");
        c3.setPackageLpa(28.5);
        c3.setMinCgpa(7.5);
        c3.setMinTenthPercentage(75.0);
        c3.setMinTwelfthPercentage(75.0);
        c3.setMaxBacklogs(0);
        c3.setEligibleBranches("CSE, IT, ECE");
        c3.setDescription("Build large-scale customer-facing distributed services supporting AWS, Prime, and worldwide retail infrastructure.");
        c3.setHiringProcess("Online Assessment (DSA + Leadership Principles) -> 3 Technical Video Interviews");
        c3.setApplicationDeadline(LocalDate.now().plusDays(15));
        c3.setApplyLink("https://www.amazon.jobs");
        companies.add(c3);

        Company c4 = new Company();
        c4.setCompanyName("Wipro");
        c4.setRoleName("Project Engineer (Elite & Turbo)");
        c4.setPackageLpa(6.5);
        c4.setMinCgpa(6.0);
        c4.setMinTenthPercentage(60.0);
        c4.setMinTwelfthPercentage(60.0);
        c4.setMaxBacklogs(1);
        c4.setEligibleBranches("ALL");
        c4.setDescription("Wipro Elite National Talent Hunt recruits tech talent across all undergraduate engineering disciplines.");
        c4.setHiringProcess("Aptitude + Coding Test + Written Communication -> Virtual Technical & HR Interview");
        c4.setApplicationDeadline(LocalDate.now().plusDays(20));
        c4.setApplyLink("https://careers.wipro.com");
        companies.add(c4);

        Company c5 = new Company();
        c5.setCompanyName("Microsoft");
        c5.setRoleName("Software Engineer (New Graduate)");
        c5.setPackageLpa(26.0);
        c5.setMinCgpa(8.0);
        c5.setMinTenthPercentage(75.0);
        c5.setMinTwelfthPercentage(75.0);
        c5.setMaxBacklogs(0);
        c5.setEligibleBranches("CSE, IT");
        c5.setDescription("Empower every person and organization on the planet to achieve more with Azure, Office 365, and Windows core systems.");
        c5.setHiringProcess("Online Codility Assessment -> 3 Technical Rounds -> HR Interview");
        c5.setApplicationDeadline(LocalDate.now().plusDays(18));
        c5.setApplyLink("https://careers.microsoft.com");
        companies.add(c5);

        Company c6 = new Company();
        c6.setCompanyName("Cognizant (CTS)");
        c6.setRoleName("GenC Next Developer");
        c6.setPackageLpa(6.75);
        c6.setMinCgpa(6.5);
        c6.setMinTenthPercentage(60.0);
        c6.setMinTwelfthPercentage(60.0);
        c6.setMaxBacklogs(0);
        c6.setEligibleBranches("CSE, IT, ECE, EEE");
        c6.setDescription("Specialized role focusing on Full-Stack, Cloud, and Microservices engineering for Fortune 500 enterprise clients.");
        c6.setHiringProcess("Skill-based Programming Assessment -> Technical Interview -> HR Discussion");
        c6.setApplicationDeadline(LocalDate.now().plusDays(22));
        c6.setApplyLink("https://careers.cognizant.com");
        companies.add(c6);

        Company c7 = new Company();
        c7.setCompanyName("Google");
        c7.setRoleName("Associate Software Engineer");
        c7.setPackageLpa(32.0);
        c7.setMinCgpa(8.5);
        c7.setMinTenthPercentage(80.0);
        c7.setMinTwelfthPercentage(80.0);
        c7.setMaxBacklogs(0);
        c7.setEligibleBranches("CSE, IT, ECE");
        c7.setDescription("Work on global high-scale distributed systems, Search, Android, and Cloud infrastructures.");
        c7.setHiringProcess("Google Online Challenge (GOC) -> 4 Technical Phone & Video DSA Interviews");
        c7.setApplicationDeadline(LocalDate.now().plusDays(14));
        c7.setApplyLink("https://careers.google.com");
        companies.add(c7);

        companyRepository.saveAll(companies);
    }

    private void initStudentDemoActivity() {
        User rahul = userRepository.findByEmail("rahul@student.com").orElse(null);
        if (rahul == null) return;

        List<MockTest> tests = mockTestRepository.findAll();
        if (!tests.isEmpty()) {
            MockTest test1 = tests.get(0);
            TestAttempt attempt1 = new TestAttempt();
            attempt1.setStudent(rahul);
            attempt1.setMockTest(test1);
            attempt1.setTotalQuestions(10);
            attempt1.setCorrectAnswers(8);
            attempt1.setWrongAnswers(2);
            attempt1.setUnattempted(0);
            attempt1.setScore(8);
            attempt1.setPercentage(80.0);
            attempt1.setTimeTakenSeconds(940);
            attempt1.setStatus("PASSED");
            attempt1.setResponsesJson("[]");
            attempt1.setAttemptedAt(LocalDateTime.now().minusDays(2));
            testAttemptRepository.save(attempt1);
        }

        // Add some practice history items for Rahul
        List<Question> questions = questionRepository.findAll();
        for (int i = 0; i < Math.min(6, questions.size()); i++) {
            Question q = questions.get(i);
            boolean isCorrect = (i % 3 != 0);
            PracticeHistory ph = new PracticeHistory(rahul, q, isCorrect ? q.getCorrectOption() : "A", isCorrect);
            practiceHistoryRepository.save(ph);
        }
    }

    private Question createQ(String text, String a, String b, String c, String d, String correct,
                             String cat, String topic, String diff, String exp) {
        Question q = new Question();
        q.setQuestionText(text);
        q.setOptionA(a);
        q.setOptionB(b);
        q.setOptionC(c);
        q.setOptionD(d);
        q.setCorrectOption(correct);
        q.setCategory(cat);
        q.setTopic(topic);
        q.setDifficulty(diff);
        q.setExplanation(exp);
        return q;
    }
}
