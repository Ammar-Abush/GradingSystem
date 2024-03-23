package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class StudentInfoDao implements StudentDaoInterface {

    private static final String GET_STUDENT_INFO_QUERY = "SELECT ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId FROM Student WHERE ssn = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public StudentInfo getStudentInfo(String ssn) {
        return jdbcTemplate.queryForObject(GET_STUDENT_INFO_QUERY, new Object[]{ssn}, new BeanPropertyRowMapper<>(StudentInfo.class));
    }
    @Override
    public boolean validate(StudentInfo student) {
        String sql = "SELECT 1 FROM Student WHERE ssn = ? AND password = ?";

        try {
            String passwordHashed = hashPassword(student.getPassword());
            jdbcTemplate.queryForObject(sql, new Object[]{student.getSsn(), passwordHashed}, Boolean.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void registerStudent(String ssn, String firstName, String mi, String lastName,  java.sql.Date birthDate, String street, String phone, String zipcode, String deptId, String password) {
        String sql = "INSERT INTO Student (ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId, Password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String passwordHashed = hashPassword(password);
        int rowsAffected = jdbcTemplate.update(sql, ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId, passwordHashed);

        if (rowsAffected > 0) {
            System.out.println("Student registered successfully!");
        } else {
            System.out.println("Failed to register student.");
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
