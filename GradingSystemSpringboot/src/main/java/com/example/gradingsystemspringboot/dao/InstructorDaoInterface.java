package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.Instructor;

import java.sql.SQLException;

public interface InstructorDaoInterface {

    Instructor getInstructorInfo(String isn) throws ClassNotFoundException, SQLException;

    boolean validate(Instructor instructor) throws ClassNotFoundException, SQLException;



}
