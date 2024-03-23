package com.example.gradingsystemservlets;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Course;
import model.Enrollment;
import model.Instructor;
import model.StudentInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InstructorProfilePage", value = "/InstructorProfilePage")

public class InstructorProfilePage extends HttpServlet{
    private TeachingDaoInterface teachingDao = new TeachingDao();
    private InstructorDaoInterface instructorInfoDao = new InstructorInfoDao();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticatedInstructor") != null && (boolean) session.getAttribute("authenticatedInstructor")) {
            String isn = (String) session.getAttribute("isn");
            if (!isn.equals(request.getParameter("isn"))){
                response.sendRedirect("Error?error=user-not-logged-in");
            } else {
                Instructor instructor = null;
                try {
                    instructor = instructorInfoDao.getInstructorInfo(isn);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
                List<Course> courses = null;
                try {
                    courses = teachingDao.getCourses(isn);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                session.setAttribute("instructorInfo", instructor);
                session.setAttribute("courses", courses);
                request.setAttribute("isn", isn);
                request.setAttribute("instructorInfo", instructor);

                getServletContext().getRequestDispatcher("/InstructorProfile.jsp").forward(request, response);
            }
        }

        else{
            request.setAttribute("error", "please sign in first");
            getServletContext().getRequestDispatcher("/InstructorLogin").forward(request, response);
        }

    }
}
