package com.tka.sams.api.service;

import com.tka.sams.api.dao.StudentDao;
import com.tka.sams.api.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentDao dao;

    public List<Student> getAllStudentsById(List<Long> studentIds) {
        return dao.getAllStudentsById(studentIds);
    }

    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    public Student createStudent(Student student) {
        return dao.createStudent(student);
    }

    public Student getStudentById(long id) {
        return dao.getStudentsById(id);
    }

    public Student updateStudent(Student studentDetails) {
        return dao.updateStudent(studentDetails);
    }

    public String deleteStudent(long id) {
        return dao.deleteStudent(id);
    }

	public List<Student> getSortedStudents(String sortBy) {
		return dao.getSortedStudents(sortBy);
	}

    public List<Student> searchStudents(String searchValue) {
        return dao.searchStudents(searchValue);
    }
}
