package com.kdr.studentManagmentSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdr.studentManagmentSystem.dto.StudentRequestDto;
import com.kdr.studentManagmentSystem.dto.StudentResponseDto;
import com.kdr.studentManagmentSystem.dto.UpdateRequestDto;
import com.kdr.studentManagmentSystem.dto.UpdateResponseDto;
import com.kdr.studentManagmentSystem.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {
	StudentService service;
	
	public StudentController(StudentService service) {
		this.service=service;
	}

	@PostMapping("/create")
	public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto student){
		StudentResponseDto createdStudent =service.createStudent(student);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<StudentResponseDto>> getStudents(){
		List<StudentResponseDto> studentRes=service.getAllStudent();
		if(studentRes==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(studentRes);
	}
	@GetMapping("/get")
	public ResponseEntity<StudentResponseDto> getStudent(@RequestParam Long id){
		StudentResponseDto studentRes=service.getOneStudent(id);
        if(studentRes==null) {
			return ResponseEntity.notFound().build();
		}
        return ResponseEntity.ok(studentRes);
	}
	@PutMapping("/update")
	public ResponseEntity<UpdateResponseDto> updateStudent(@RequestParam Long id,@RequestBody UpdateRequestDto student){
		UpdateResponseDto studentRes=service.updateStudent(id,student);
		if(studentRes==null) {
			return ResponseEntity.notFound().build();
		}
        return ResponseEntity.ok(studentRes);
	}
	@DeleteMapping("/delete")
	public ResponseEntity<String> deletePermantlyStudent(@RequestParam Long id){
		Boolean isDeleted=service.deletePermantlyStudent(id);
		if(!isDeleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok("Record Deleted");
		
	}
	@PatchMapping("/deleteSoft")
	public ResponseEntity<String> deleteSoftly(@RequestParam Long id){
		Boolean isDelete=service.deleteSoftly(id);
		if(!isDelete) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok("Response Deleted");
	}
}
