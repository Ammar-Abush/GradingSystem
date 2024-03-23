package com.example.gradingsystemspringboot.service;

import com.example.gradingsystemspringboot.dao.TeachingDao;
import com.example.gradingsystemspringboot.model.Course;
import com.example.gradingsystemspringboot.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingService {

    @Autowired
    private TeachingDao teachingDao;

    public List<Course> getCourses(String isn) {
        return teachingDao.getCourses(isn);
    }

    public List<StudentInfo> getStudents(String isn, String courseId) {
        return teachingDao.getStudents(isn, courseId);
    }
}
