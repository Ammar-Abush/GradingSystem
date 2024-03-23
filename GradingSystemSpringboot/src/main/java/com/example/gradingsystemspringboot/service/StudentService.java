package com.example.gradingsystemspringboot.service;

import com.example.gradingsystemspringboot.dao.StudentInfoDao;
import com.example.gradingsystemspringboot.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentInfoDao studentInfoDao;
    @Autowired
    private SessionService sessionService;
    public boolean validate(StudentInfo student) {
        boolean isValid = studentInfoDao.validate(student);
        if (isValid) {
            sessionService.login(student.getSsn());
        }
        return isValid;
    }

    public StudentInfo getStudentInfo(String ssn) {
        return studentInfoDao.getStudentInfo(ssn);
    }

    public void logout(String ssn) {
        sessionService.logout(ssn);
    }

    public void RegisterStudent(String ssn, String firstName, String mi, String lastName,  java.sql.Date birthDate, String street, String phone, String zipcode, String deptId, String password){
        studentInfoDao.registerStudent(ssn, firstName,mi,lastName,birthDate,street,phone, zipcode,deptId, password);
    }

}