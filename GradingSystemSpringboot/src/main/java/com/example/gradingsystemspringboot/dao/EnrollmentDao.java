package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentDao implements EnrollmentDaoInterface {

    private static final String GET_ENROLLMENTS_QUERY = "SELECT c.title, e.grade, e.mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE e.ssn = ?";
    private static final String UPDATE_ENROLLMENT_QUERY = "UPDATE Enrollment SET mark = ?, grade = ? WHERE courseId = ? AND ssn = ?";
    private static final String COURSE_AVG_QUERY = "SELECT AVG(e.mark) AS avg_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";
    private static final String COURSE_HIGHEST_QUERY = "SELECT MAX(e.mark) AS max_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";
    private static final String COURSE_LOWEST_QUERY = "SELECT MIN(e.mark) AS min_mark FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE c.title = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Enrollment> getEnrollments(String ssn) {
        return jdbcTemplate.query(GET_ENROLLMENTS_QUERY, new Object[]{ssn}, (resultSet, rowNum) -> {
            Enrollment enrollment = new Enrollment();
            enrollment.setTitle(resultSet.getString("title"));
            enrollment.setGrade(resultSet.getString("grade"));
            enrollment.setMark(resultSet.getInt("mark"));
            enrollment.setAverage(getClassAverage(resultSet.getString("title")));
            enrollment.setHighest(getHighestMark(resultSet.getString("title")));
            enrollment.setLower(getLowestMark(resultSet.getString("title")));
            return enrollment;
        });
    }

    public void assignMarks(String isn, String courseId, String ssn, int mark, String grade) {
        jdbcTemplate.update(UPDATE_ENROLLMENT_QUERY, mark, grade, courseId, ssn);
    }

    private double getClassAverage(String courseTitle) {
        return jdbcTemplate.queryForObject(COURSE_AVG_QUERY, new Object[]{courseTitle}, Double.class);
    }

    private int getHighestMark(String courseTitle) {
        return jdbcTemplate.queryForObject(COURSE_HIGHEST_QUERY, new Object[]{courseTitle}, Integer.class);
    }

    private int getLowestMark(String courseTitle) {
        return jdbcTemplate.queryForObject(COURSE_LOWEST_QUERY, new Object[]{courseTitle}, Integer.class);
    }
}
