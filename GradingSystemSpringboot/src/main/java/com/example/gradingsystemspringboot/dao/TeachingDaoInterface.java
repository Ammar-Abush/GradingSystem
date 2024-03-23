package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.Course;
import com.example.gradingsystemspringboot.model.StudentInfo;

import java.sql.SQLException;
import java.util.List;

public interface TeachingDaoInterface {

    List<Course> getCourses(String isn) throws SQLException, ClassNotFoundException;

    List<StudentInfo> getStudents(String isn, String courseId) throws SQLException, ClassNotFoundException;


}
