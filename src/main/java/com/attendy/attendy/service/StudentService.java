package com.attendy.attendy.service;

import com.attendy.attendy.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student saveStudent(Student student);
    void deleteStudent(Long id);
}
