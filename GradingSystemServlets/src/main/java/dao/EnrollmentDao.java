package dao;

import jakarta.validation.constraints.Null;
import model.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao implements EnrollmentDaoInterface{

    private static final String GET_ENROLLMENTS_QUERY = "SELECT c.title, e.grade, e.mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE e.ssn = ?";
    private static final String JDBC_URL = "jdbc:mysql://localhost/GradingSystem";
    private static final String USER = "Ammar";
    private static final String PASSWORD = "falcon";

    public EnrollmentDao() throws SQLException {
    }

    public  List<Enrollment> getEnrollments(String ssn) throws ClassNotFoundException, SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("hello" + ssn);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ENROLLMENTS_QUERY)) {

            preparedStatement.setString(1, ssn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    Enrollment enrollment = new Enrollment();
                    enrollment.setTitle(resultSet.getString("title"));
                    enrollment.setGrade(resultSet.getString("grade"));
                    enrollment.setMark(resultSet.getInt("mark"));
                    enrollment.setAverage(getClassAverage(connection, resultSet.getString("title")));
                    enrollment.setHighest(getHighestMark(connection, resultSet.getString("title")));
                    enrollment.setLower(getLoweestMark(connection, resultSet.getString("title")));
                    enrollments.add(enrollment);
                }
            }
        }

        return enrollments;
    }
    private static double getClassAverage(Connection connection, String courseTitle) throws SQLException {
        String courseAvgQuery = "SELECT AVG(e.mark) AS avg_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";
        try (PreparedStatement courseAvgStatement = connection.prepareStatement(courseAvgQuery)) {
            courseAvgStatement.setString(1, courseTitle);
            ResultSet courseAvgResult = courseAvgStatement.executeQuery();

            return courseAvgResult.next() ? courseAvgResult.getDouble("avg_mark") : 0.0;
        }
    }

    private static int getHighestMark(Connection connection, String courseTitle) throws SQLException{
        String courseHighestQuery = "SELECT MAX(e.mark) AS max_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";
        try (PreparedStatement courseHighestStatement = connection.prepareStatement(courseHighestQuery)) {
            courseHighestStatement.setString(1, courseTitle);
            ResultSet courseHighestResult = courseHighestStatement.executeQuery();

            return courseHighestResult.next() ? courseHighestResult.getInt("max_mark") : null;
        }
    }
    private static int getLoweestMark(Connection connection, String courseTitle) throws SQLException{
        String courseLowestQuery = "SELECT MIN(e.mark) AS max_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";
        try (PreparedStatement courseLowestStatement = connection.prepareStatement(courseLowestQuery)) {
            courseLowestStatement.setString(1, courseTitle);
            ResultSet courseLowestResult = courseLowestStatement.executeQuery();

            return courseLowestResult.next() ? courseLowestResult.getInt("max_mark") : null;
        }
    }
    public  void assignMarks(String isn, String courseId, String ssn, int mark, String grade) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println(ssn + " "+ " "+ courseId + " "+ mark + " " + grade);
        String query = "UPDATE Enrollment " +
                "SET mark = ?, grade = ? " +
                "WHERE courseId = ? AND ssn = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mark);
            preparedStatement.setString(2, grade);
            preparedStatement.setString(3, courseId);
            preparedStatement.setString(4, ssn);

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
