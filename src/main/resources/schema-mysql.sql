-- ===================================================================
-- PLACEMENT PREPARATION PORTAL - MYSQL DATABASE SCHEMA
-- College Project Full-Stack Java (Spring Boot & MySQL)
-- ===================================================================

CREATE DATABASE IF NOT EXISTS placement_portal;
USE placement_portal;

-- 1. Users Table (Students & Administrators)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_STUDENT',
    phone VARCHAR(20),
    college VARCHAR(200),
    degree VARCHAR(50),
    branch VARCHAR(100),
    cgpa DOUBLE,
    tenth_percentage DOUBLE,
    twelfth_percentage DOUBLE,
    active_backlogs INT DEFAULT 0,
    passing_year INT,
    skills TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Questions Bank (Aptitude, Reasoning, Verbal, Technical)
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option VARCHAR(2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    topic VARCHAR(100),
    difficulty VARCHAR(20) NOT NULL,
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Mock Tests Table
CREATE TABLE IF NOT EXISTS mock_tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    total_marks INT,
    passing_marks INT,
    category VARCHAR(50),
    company_tag VARCHAR(50),
    difficulty VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Mock Test Questions Mapping Table
CREATE TABLE IF NOT EXISTS mock_test_questions (
    mock_test_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    PRIMARY KEY (mock_test_id, question_id),
    FOREIGN KEY (mock_test_id) REFERENCES mock_tests(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- 5. Student Test Attempts
CREATE TABLE IF NOT EXISTS test_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mock_test_id BIGINT NOT NULL,
    total_questions INT,
    correct_answers INT,
    wrong_answers INT,
    unattempted INT,
    score INT,
    percentage DOUBLE,
    time_taken_seconds INT,
    status VARCHAR(20),
    responses_json TEXT,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mock_test_id) REFERENCES mock_tests(id) ON DELETE CASCADE
);

-- 6. DSA Coding Problems
CREATE TABLE IF NOT EXISTS coding_problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    starter_code TEXT NOT NULL,
    sample_input TEXT,
    sample_output TEXT,
    test_cases_json TEXT,
    solution_approach TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Code Submissions
CREATE TABLE IF NOT EXISTS code_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    submitted_code TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    test_cases_passed INT,
    total_test_cases INT,
    execution_output TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (problem_id) REFERENCES coding_problems(id) ON DELETE CASCADE
);

-- 8. Recruiter Companies & Criteria
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    role_name VARCHAR(150) NOT NULL,
    package_lpa DOUBLE NOT NULL,
    min_cgpa DOUBLE NOT NULL,
    min_tenth_percentage DOUBLE NOT NULL,
    min_twelfth_percentage DOUBLE NOT NULL,
    max_backlogs INT NOT NULL DEFAULT 0,
    eligible_branches VARCHAR(255),
    description TEXT,
    hiring_process TEXT,
    application_deadline DATE,
    apply_link VARCHAR(255),
    logo_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. Resumes Table
CREATE TABLE IF NOT EXISTS resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(20),
    location VARCHAR(100),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    summary TEXT,
    degree VARCHAR(50),
    branch VARCHAR(100),
    college VARCHAR(200),
    cgpa DOUBLE,
    passing_year INT,
    twelfth_school VARCHAR(200),
    twelfth_percentage DOUBLE,
    twelfth_year INT,
    tenth_school VARCHAR(200),
    tenth_percentage DOUBLE,
    tenth_year INT,
    technical_skills TEXT,
    soft_skills TEXT,
    tools_and_databases TEXT,
    project1_title VARCHAR(200),
    project1_tech VARCHAR(200),
    project1_desc TEXT,
    project1_link VARCHAR(255),
    project2_title VARCHAR(200),
    project2_tech VARCHAR(200),
    project2_desc TEXT,
    project2_link VARCHAR(255),
    exp_role VARCHAR(100),
    exp_company VARCHAR(100),
    exp_duration VARCHAR(50),
    exp_desc TEXT,
    certifications TEXT,
    achievements TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 10. Practice History Table
CREATE TABLE IF NOT EXISTS practice_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option VARCHAR(2) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    practiced_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);
