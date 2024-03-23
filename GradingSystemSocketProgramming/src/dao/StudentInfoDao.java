package dao;

import model.StudentInfo;

import java.sql.*;
public class StudentInfoDao implements StudentDaoInterface {

    private static final String GET_STUDENT_INFO_QUERY = "SELECT ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId FROM Student WHERE ssn = ?";
    private static final String JDBC_URL = "jdbc:mysql://localhost/GradingSystem";
    private static final String USER = "Ammar";
    private static final String PASSWORD = "falcon";

    public StudentInfo getStudentInfo(String ssn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT_INFO_QUERY)) {

            preparedStatement.setString(1, ssn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the result set to a StudentInfo object
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setSsn(resultSet.getString("ssn"));
                    studentInfo.setFirstName(resultSet.getString("firstName"));
                    studentInfo.setMi(resultSet.getString("mi"));
                    studentInfo.setLastName(resultSet.getString("lastName"));
                    studentInfo.setStreet(resultSet.getString("street"));
                    studentInfo.setPhone(resultSet.getString("phone"));
                    studentInfo.setZipcode(resultSet.getString("zipcode"));
                    studentInfo.setDeptId(resultSet.getString("deptId"));
                    // Add other attributes as needed

                    return studentInfo;
                }
            }
        }

        return null; // If no student with the given SSN is found
    }
    public boolean validate(StudentInfo student) throws ClassNotFoundException, SQLException {
        boolean status = false;
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("select * from Student where ssn = ? and password = ? ")) {

            preparedStatement.setString(1, student.getSsn());
            preparedStatement.setString(2, student.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            status = rs.next();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public void registerStudent(String ssn, String firstName, String mi, String lastName, Date birthDate, String street, String phone, String zipcode, String deptId, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Student (ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId, Password) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            // Set values for the parameters in the SQL statement
            preparedStatement.setString(1, ssn);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, mi);  // Assuming you have the variable 'mi' defined
            preparedStatement.setString(4, lastName);
            preparedStatement.setDate(5, birthDate);
            preparedStatement.setString(6, street);
            preparedStatement.setString(7, phone);
            preparedStatement.setString(8, zipcode);
            preparedStatement.setString(9, deptId);
            preparedStatement.setString(10, password);

            // Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student registered successfully!");
            } else {
                System.out.println("Failed to register student.");
            }
        }
    }
}