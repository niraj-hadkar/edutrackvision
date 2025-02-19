package com.tka.sams.api.controller;

import com.tka.sams.api.entity.Student;
import com.tka.sams.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin("http://localhost:4200")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/get-all-students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/add-student")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/get-student-by-id/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/update-student")
    public Student updateStudent(@RequestBody Student studentDetails) {
        return studentService.updateStudent(studentDetails);
    }

    @DeleteMapping("/delete-student/{id}")
    public String deleteStudent(@PathVariable long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/get-sorted-students")
    public List<Student> getSortedStudents(@RequestParam String sortBy) {
        return studentService.getSortedStudents(sortBy);
    }

    @GetMapping("/search-students")
    public List<Student> searchStudents(@RequestParam String query) {
        return studentService.searchStudents(query);
    }
}
