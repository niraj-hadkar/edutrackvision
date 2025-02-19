package com.tka.sams.api.dao;

import com.tka.sams.api.entity.AttendanceRecord;
import com.tka.sams.api.entity.Student;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private SessionFactory factory;

    public List<Student> getAllStudentsById(List<Long> studentIds) {
        Session session = null;
        List<Student> students = null;
        try {
            session = factory.openSession();
            students = session.byMultipleIds(Student.class).multiLoad(studentIds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return students;
    }

    public List<Student> getAllStudents() {
        Session session = null;
        List<Student> list = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(Student.class);
            list = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public Student createStudent(Student student) {
        Session session = null;
        Student s = null;
        try {
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            s = student;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return s;
    }

    public Student getStudentsById(long id) {
        Session session = null;
        Student student = null;
        try {
            session = factory.openSession();
            student = session.get(Student.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return student;
    }

    public Student updateStudent(Student studentDetails) {
        Session session = null;
        Student s = null;
        try {
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(studentDetails);
            transaction.commit();
            s = studentDetails;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return s;
    }

    public String deleteStudent(long id) {
        Session session = null;
        String msg = null;
        try {
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();

            // Find the student
            Student student = session.get(Student.class, id);
            if (student != null) {

                // Fetch all attendance records where this student is present
                Query query = session.createQuery(
                        "SELECT a FROM AttendanceRecord a WHERE :student MEMBER OF a.students",
                        AttendanceRecord.class
                );
                query.setParameter("student", student);
                List<AttendanceRecord> attendanceRecords = query.getResultList();

                // Remove student from each attendance record and update count
                for (AttendanceRecord attendanceRecord : attendanceRecords) {
                    attendanceRecord.getStudents().remove(student); // Remove student
                    attendanceRecord.setNumberOfStudents(attendanceRecord.getStudents().size()); // Update count
                    session.update(attendanceRecord); // Save updated attendance record
                }

                // Delete the student
                session.delete(student);
                transaction.commit();
                msg = "Student deleted successfully!";
            } else {
                msg = "Student does not exist!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return msg;
    }

    public List<Student> getSortedStudents(String sortBy) {
        Session session = null;
        List<Student> sortedStudents = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(Student.class);

            if ("name".equalsIgnoreCase(sortBy)) {
                criteria.addOrder(Order.asc("name"));
            } else if ("email".equalsIgnoreCase(sortBy)) {
                criteria.addOrder(Order.asc("email"));
            }

            sortedStudents = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return sortedStudents;
    }

    public List<Student> searchStudents(String searchValue) {
        Session session = null;
        List<Student> matchedStudents = null;
        try {
            session = factory.openSession();
            Criteria criteria = session.createCriteria(Student.class);

            Disjunction orConditions = Restrictions.disjunction();
            orConditions.add(Restrictions.ilike("name", "%" + searchValue + "%"));
            orConditions.add(Restrictions.ilike("email", "%" + searchValue + "%"));

            criteria.add(orConditions);

            matchedStudents = criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return matchedStudents;
    }

}
