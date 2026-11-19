package com.kdr.studentManagmentSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kdr.studentManagmentSystem.dto.StudentRequestDto;
import com.kdr.studentManagmentSystem.dto.StudentResponseDto;
import com.kdr.studentManagmentSystem.dto.UpdateRequestDto;
import com.kdr.studentManagmentSystem.dto.UpdateResponseDto;
import com.kdr.studentManagmentSystem.entity.Student;
import com.kdr.studentManagmentSystem.repository.StudentRepository;

@Service
public class StudentService {
	StudentRepository repo;
	public StudentService(StudentRepository repo) {
		this.repo=repo;
	}
//this is use for creating new student
	public StudentResponseDto createStudent(StudentRequestDto studentReq) {
		Student student=mapToEntity(studentReq);
		student.setCreatedAt(LocalDateTime.now());
		student.setUpdatedAt(LocalDateTime.now());
		Student studentRes=repo.save(student);
		return maptoDto(studentRes);
	}
	public List<StudentResponseDto> getAllStudent() {
		List<Student> studentRes=repo.findAll();
		if(studentRes.isEmpty()) {
			return null;
		}
		return studentRes.stream()
	            .map(this::maptoDto)
	            .toList();
	}
	public StudentResponseDto getOneStudent(Long id) {
		Optional<Student> studentRes=repo.findById(id);
		if(studentRes.isPresent()) {
			Student s=studentRes.get();
			return maptoDto(s);
		}
		return null;
	}
	public UpdateResponseDto updateStudent(Long id, UpdateRequestDto student) {
		Optional<Student> studentres=repo.findByIdAndDeletedIsfalse(id);
		if(studentres.isEmpty()) {
			return null;
		}
		Student save =studentres.get();
		save.setAge(student.getAge());
		save.setName(student.getName());
		save.setRollNo(student.getRollNo());
		save.setSubject(student.getSubject());
		save.setUpdatedAt(LocalDateTime.now());
		Student update= repo.save(save);
		return mapToUpdateDto(update);
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

	private Student mapToEntity(StudentRequestDto studentReq) {
		Student s=new Student();
		s.setName(studentReq.getName());
		s.setAge(studentReq.getAge());
		s.setEmail(studentReq.getEmail());
		s.setRollNo(studentReq.getRollNo());
		s.setSubject(studentReq.getSubject());
		s.setDeleted(false);
		return s;
	}
	private StudentResponseDto maptoDto(Student student) {
		StudentResponseDto dto=new StudentResponseDto();
		dto.setId(student.getId());
		dto.setName(student.getName());
		dto.setAge(student.getAge());
		dto.setEmail(student.getEmail());
		dto.setRollNo(student.getRollNo());
		dto.setSubject(student.getSubject());
		dto.setMessage("Student save Successfully");
		dto.setCreatedAt(student.getCreatedAt());
		dto.setUpdatedAt(student.getUpdatedAt());
		return dto;
	}
	private UpdateResponseDto mapToUpdateDto(Student s) {
		UpdateResponseDto d=new UpdateResponseDto();
		d.setId(s.getId());
		d.setName(s.getName());
		d.setAge(s.getAge());
		d.setRollNo(s.getRollNo());
		d.setSubject(s.getSubject());
		d.setUpdatedAt(LocalDateTime.now());
		d.setMessage("Updated Successfully");
		return d;
	}
}
