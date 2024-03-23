package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class InstructorInfoDao implements InstructorDaoInterface {
    private static final String GET_INSTRUCTOR_INFO_QUERY = "SELECT isn, firstName, mi, lastName, deptId, instructorRank FROM Instructor WHERE isn = ?";
    private static final String VALIDATE_INSTRUCTOR_QUERY = "SELECT * FROM Instructor WHERE isn = ? AND password = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Instructor getInstructorInfo(String isn) {
        return jdbcTemplate.queryForObject(GET_INSTRUCTOR_INFO_QUERY, new Object[]{isn}, (resultSet, i) -> {
            Instructor instructor = new Instructor();
            instructor.setIsn(resultSet.getString("isn"));
            instructor.setFirstName(resultSet.getString("firstName"));
            instructor.setMi(resultSet.getString("mi"));
            instructor.setLastName(resultSet.getString("lastName"));
            instructor.setDeptId(resultSet.getString("deptId"));
            instructor.setInstructorRank(resultSet.getString("instructorRank"));
            return instructor;
        });
    }

    @Override
    public boolean validate(Instructor instructor) {
        String sql = "SELECT 1 FROM Instructor WHERE isn = ? AND password = ?";

        try {
            String passwordHashed = hashPassword(instructor.getPassword());
            jdbcTemplate.queryForObject(sql, new Object[]{instructor.getIsn(), passwordHashed}, Boolean.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
