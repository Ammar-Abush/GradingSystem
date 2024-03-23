package com.example.gradingsystemspringboot.service;

import com.example.gradingsystemspringboot.dao.InstructorInfoDao;
import com.example.gradingsystemspringboot.dao.StudentInfoDao;
import com.example.gradingsystemspringboot.model.Instructor;
import com.example.gradingsystemspringboot.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {

    @Autowired
    private InstructorInfoDao instructorInfoDao;
    @Autowired
    private SessionService sessionService;
    public boolean validate(Instructor instructor) {
        boolean isValid = instructorInfoDao.validate(instructor);
        if (isValid) {
            sessionService.login(instructor.getIsn());
        }
        return isValid;
    }

    public Instructor getInstructorInfo(String isn) {
        return instructorInfoDao.getInstructorInfo(isn);
    }

    public void logout(String isn) {
        sessionService.logout(isn);
    }

}