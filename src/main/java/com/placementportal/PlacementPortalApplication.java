package com.placementportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for Placement Preparation Portal.
 * 
 * College Project: Placement Preparation Portal
 * Built with: Spring Boot 3, Spring Data JPA, Spring Security, Thymeleaf, MySQL/H2
 */
@SpringBootApplication
public class PlacementPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementPortalApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("  Placement Preparation Portal Started Successfully!");
        System.out.println("  Access Web Application: http://localhost:8088");
        System.out.println("  Default Admin Login:   admin@portal.com / admin123");
        System.out.println("  Default Student Login: rahul@student.com / student123");
        System.out.println("  H2 Console (if using H2): http://localhost:8088/h2-console");
        System.out.println("=================================================================");
    }
}
