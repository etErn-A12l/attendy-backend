package com.attendy.attendy.controller;

import com.attendy.attendy.dto.LoginRequest;
import com.attendy.attendy.entity.Student;
import com.attendy.attendy.service.StudentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final StudentService studentService;

    private static final String SECRET_KEY = "9J5H9705gABGQ7McnT09Lq9aV4eQshzAJieM26WhWBhkFBZCnxc4V0EZynkkJm0u1LfH0a6RsmG";

    public LoginController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Student student = studentService.findByEmail(loginRequest.getEmail());

        if (student == null) {
            return ResponseEntity.notFound().build();
        } else if (!student.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String JwtToken = generateJwtToken(student.getEmail());

        // Create an HTTP-only cookie with the JWT token
        ResponseCookie cookie = ResponseCookie.from("AttendyRest", JwtToken)
                .httpOnly(true)
                .maxAge(3600) // 1 hour in seconds
                .path("/")
                .build();

        // Add the cookie to the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        return ResponseEntity.ok().headers(headers).body(response);

        // return ResponseEntity.ok().body("Login successful");
    }

    private String generateJwtToken(String userEmail) {
        long expirationTimeInMs = 3600000L * 24 * 30 * 2; // 1 hour * 1 Day * 30 Days * 2 Months = 2 Months
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInMs);

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }
}
