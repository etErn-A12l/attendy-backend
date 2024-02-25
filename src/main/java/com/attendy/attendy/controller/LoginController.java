package com.attendy.attendy.controller;

import com.attendy.attendy.dto.LoginRequest;
import com.attendy.attendy.entity.Student;
import com.attendy.attendy.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final StudentService studentService;

    public LoginController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Student student = studentService.findByEmail(loginRequest.getEmail());

        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        else if (!student.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok().body("Login successful");
    }
}
