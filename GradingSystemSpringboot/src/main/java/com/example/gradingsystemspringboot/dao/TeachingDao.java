package com.example.gradingsystemspringboot.dao;

import com.example.gradingsystemspringboot.model.Course;
import com.example.gradingsystemspringboot.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeachingDao implements TeachingDaoInterface {
    private static final String GET_COURSES_QUERY = "SELECT c.title, c.courseId, c.numOfCredits, c.subjectId FROM Course c JOIN Teaching e ON c.courseId = e.courseId WHERE e.isn = ?";
    private static final String GET_NUM_STUDENTS_IN_COURSE_QUERY = "SELECT COUNT(DISTINCT e.ssn) AS numStudents FROM Teaching t JOIN Enrollment e ON t.courseId = e.courseId WHERE t.isn = ? AND t.courseId = ?";
    private static final String GET_STUDENTS_QUERY = "SELECT e.ssn, s.firstName, s.lastName FROM Teaching t JOIN Enrollment e ON t.courseId = e.courseId JOIN Student s ON e.ssn = s.ssn WHERE t.isn = ? AND t.courseId = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Course> getCourses(String isn) {
        return jdbcTemplate.query(GET_COURSES_QUERY, new Object[]{isn}, (resultSet, i) -> {
            Course course = new Course();
            course.setCourseId(resultSet.getString("courseId"));
            course.setTitle(resultSet.getString("title"));
            course.setNumOfCredits(resultSet.getInt("numOfCredits"));
            course.setSubjectId(resultSet.getString("subjectId"));
            course.setNumOfStudents(getNumStudentsInCourse(isn, resultSet.getString("courseId")));
            return course;
        });
    }

    private int getNumStudentsInCourse(String isn, String courseId) {
        return jdbcTemplate.queryForObject(GET_NUM_STUDENTS_IN_COURSE_QUERY, new Object[]{isn, courseId}, Integer.class);
    }

    @Override
    public List<StudentInfo> getStudents(String isn, String courseId) {
        return jdbcTemplate.query(GET_STUDENTS_QUERY, new Object[]{isn, courseId}, (resultSet, i) -> {
            StudentInfo student = new StudentInfo();
            student.setSsn(resultSet.getString("ssn"));
            student.setFirstName(resultSet.getString("firstName"));
            student.setLastName(resultSet.getString("lastName"));
            return student;
        });
    }
}
