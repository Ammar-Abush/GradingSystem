package dao;

import model.Course;
import model.StudentInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeachingDao implements TeachingDaoInterface{
    private static final String JDBC_URL = "jdbc:mysql://localhost/GradingSystem";
    private static final String USER = "Ammar";
    private static final String PASSWORD = "falcon";
    private static String query = "SELECT c.title, c.courseId, c.numOfCredits, c.subjectId FROM Course c JOIN Teaching e ON c.courseId = e.courseId WHERE e.isn = ?";

    public List<Course> getCourses(String isn) throws SQLException, ClassNotFoundException {
        List<Course> courses = new ArrayList<>();
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, isn);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String courseTitle = resultSet.getString("title");
                    String courseId = resultSet.getString("courseId");
                    String subjectId = resultSet.getString("subjectId");
                    int numOfCredits = resultSet.getInt("numOfCredits");
                    Course course = new Course();
                    course.setCourseId(courseId);
                    course.setTitle(courseTitle);
                    course.setNumOfCredits(numOfCredits);
                    course.setSubjectId(subjectId);
                    course.setNumOfStudents(getNumStudentsInCourse(connection, isn, courseId));
                    courses.add(course);
                }
            }

        }
        return courses;
    }

    private static int getNumStudentsInCourse(Connection connection, String isn, String courseId) throws SQLException {
        int numStudents = 0;

        String query = "SELECT COUNT(DISTINCT e.ssn) AS numStudents " +
                "FROM Teaching t " +
                "JOIN Enrollment e ON t.courseId = e.courseId " +
                "WHERE t.isn = ? AND t.courseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, isn);
            preparedStatement.setString(2, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    numStudents = resultSet.getInt("numStudents");
                }
            }
        }

        return numStudents;
    }

    public List<StudentInfo> getStudents(String isn, String courseId) throws SQLException, ClassNotFoundException {
        List<StudentInfo> students = new ArrayList<>();
        System.out.println(isn + " " + courseId);
        String query = "SELECT e.ssn, s.firstName, s.lastName " +
                "FROM Teaching t " +
                "JOIN Enrollment e ON t.courseId = e.courseId " +
                "JOIN Student s ON e.ssn = s.ssn " +
                "WHERE t.isn = ? AND t.courseId = ?";

        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, isn);
                preparedStatement.setString(2, courseId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StudentInfo student = new StudentInfo();
                        String ssn = resultSet.getString("ssn");
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        System.out.println(firstName);
                        student.setSsn(ssn);
                        student.setFirstName(firstName);
                        student.setLastName(lastName);
                        students.add(student);
                    }
                }
            }

            return students;
        }


    }
}