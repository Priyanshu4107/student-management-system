package com.kdr.studentManagmentSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdr.studentManagmentSystem.entity.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	Optional<Student> findByIdAndDeletedIsfalse(Long id);
}
