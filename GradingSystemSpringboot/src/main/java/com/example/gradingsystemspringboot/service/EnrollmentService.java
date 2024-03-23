package com.example.gradingsystemspringboot.service;

import com.example.gradingsystemspringboot.dao.EnrollmentDaoInterface;
import com.example.gradingsystemspringboot.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentDaoInterface enrollmentDao;

    @Autowired
    public EnrollmentService(EnrollmentDaoInterface enrollmentDao) {
        this.enrollmentDao = enrollmentDao;
    }

    public List<Enrollment> getEnrollments(String ssn) throws SQLException, ClassNotFoundException {
        List<Enrollment> enrollments =  enrollmentDao.getEnrollments(ssn);
        System.out.println("Enrollments");
        System.out.println(enrollments);
        return enrollments;
    }

    public void assignMarks(String isn, String courseId, String ssn, int mark, String grade) throws ClassNotFoundException {
        enrollmentDao.assignMarks(isn, courseId, ssn, mark, grade);
    }
}
