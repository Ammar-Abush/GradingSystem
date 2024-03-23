package dao;

import model.Instructor;
import model.StudentInfo;

import java.sql.*;

public class InstructorInfoDao implements InstructorDaoInterface{
    private static final String JDBC_URL = "jdbc:mysql://localhost/GradingSystem";
    private static final String USER = "Ammar";
    private static final String PASSWORD = "falcon";
    private static final String GET_INSTRUCTOR_INFO_QUERY = "SELECT Isn, firstName, mi, lastName,deptId,instructorRank  FROM Instructor WHERE isn = ?";
    public Instructor getInstructorInfo(String isn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_INSTRUCTOR_INFO_QUERY)) {

            preparedStatement.setString(1, isn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    Instructor instructor = new Instructor();
                    instructor.setIsn(resultSet.getString("isn"));
                    instructor.setFirstName(resultSet.getString("firstName"));
                    instructor.setMi(resultSet.getString("mi"));
                    instructor.setLastName(resultSet.getString("lastName"));
                    instructor.setDeptId(resultSet.getString("deptId"));
                    instructor.setInstructorRank(resultSet.getString("instructorRank"));


                    return instructor;
                }
            }
        }
        return null;
    }
    public boolean validate(Instructor instructor) throws ClassNotFoundException, SQLException {
        boolean status = false;
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("select * from Instructor where isn = ? and password = ? ")) {

            preparedStatement.setString(1, instructor.getIsn());
            preparedStatement.setString(2, instructor.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            status = rs.next();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }
}
