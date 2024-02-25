package com.attendy.attendy.controller;

import com.attendy.attendy.entity.Student;
import com.attendy.attendy.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VerifyController {

    private final StudentService studentService;
    private static final String SECRET_KEY = "9J5H9705gABGQ7McnT09Lq9aV4eQshzAJieM26WhWBhkFBZCnxc4V0EZynkkJm0u1LfH0a6RsmG";

    public VerifyController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/api/verify")
    public ResponseEntity<Object> verifyToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found.");
        }

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        for (Cookie cookie : cookies) {
            if ("AttendyRest".equals(cookie.getName())) {
                String JwtToken = cookie.getValue();
                try {
                    Jws<Claims> claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(JwtToken);

                    // Extract relevant information from claims (user email)
                    String userEmail = claims.getPayload().getSubject();

                    System.out.println(userEmail);

                    Student student = studentService.findByEmail(userEmail);
                    if (student == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    String studentJson = objectMapper.writeValueAsString(student);

                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Login successful");
                    response.put("user", studentJson);
                    return ResponseEntity.ok().body(response);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT cookie found.");
    }
}
