package dao;

import model.StudentInfo;

import java.sql.SQLException;

public interface StudentDaoInterface {

    StudentInfo getStudentInfo(String ssn) throws ClassNotFoundException, SQLException;

    boolean validate(StudentInfo student) throws ClassNotFoundException, SQLException;

    void registerStudent(String ssn, String firstName, String mi, String lastName,
                         java.sql.Date birthDate, String street, String phone, String zipcode,
                         String deptId, String password) throws ClassNotFoundException, SQLException;



}
