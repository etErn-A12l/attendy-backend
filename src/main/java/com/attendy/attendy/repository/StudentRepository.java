package com.attendy.attendy.repository;

import com.attendy.attendy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
