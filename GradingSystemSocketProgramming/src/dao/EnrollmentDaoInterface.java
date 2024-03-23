package dao;

import model.Enrollment;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentDaoInterface {

    List<Enrollment> getEnrollments(String ssn) throws ClassNotFoundException, SQLException;

    void assignMarks(String isn, String courseId, String ssn, int mark, String grade) throws ClassNotFoundException;


}
