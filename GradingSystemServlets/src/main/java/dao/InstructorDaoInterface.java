package dao;

import model.Instructor;

import java.sql.SQLException;

public interface InstructorDaoInterface {

    Instructor getInstructorInfo(String isn) throws ClassNotFoundException, SQLException;

    boolean validate(Instructor instructor) throws ClassNotFoundException, SQLException;



}
