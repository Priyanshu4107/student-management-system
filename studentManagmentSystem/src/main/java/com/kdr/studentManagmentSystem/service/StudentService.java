package com.kdr.studentManagmentSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kdr.studentManagmentSystem.entity.Student;
import com.kdr.studentManagmentSystem.repository.StudentRepository;

@Service
public class StudentService {
	StudentRepository repo;
	public StudentService(StudentRepository repo) {
		this.repo=repo;
	}
//this is use for creating new student
	public Student createStudent(Student studentReq) {
		studentReq.setDeleted(false);
		Student studentRes=repo.save(studentReq);
		return studentRes;
	}
	public List<Student> getAllStudent() {
		List<Student> studentRes=repo.findAll();
		if(studentRes.isEmpty()) {
			return null;
		}
		return studentRes;
	}
	public Student getOneStudent(Long id) {
		Optional<Student> studentRes=repo.findById(id);
		if(studentRes.isPresent()) {
			return studentRes.get();
		}
		return null;
	}
	public Student updateStudent(Long id, Student student) {
		Optional<Student> studentres=repo.findByIdAndDeletedIsfalse(id);
		if(studentres.isEmpty()) {
			return null;
		}
		Student save =studentres.get();
		save.setAge(student.getAge());
		save.setName(student.getName());
		save.setEmail(student.getEmail());
		save.setRollNo(student.getRollNo());
		save.setSubject(student.getSubject());
		return repo.save(save);
	}
	public Boolean deletePermantlyStudent(Long id) {
		Boolean isStudent=repo.existsById(id);
		if(!isStudent) {
			return false;
		}
		repo.deleteById(id);
		return true;
	}
	public Boolean deleteSoftly(Long id) {
		Optional<Student> existingStudent=repo.findByIdAndDeletedIsfalse(id);
		if(existingStudent.isEmpty()) {
		return null;
		}
		Student studentToSave=existingStudent.get();
		studentToSave.setDeleted(true);
		repo.save(studentToSave);
		return true;
	}

}
