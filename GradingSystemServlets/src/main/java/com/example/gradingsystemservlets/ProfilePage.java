package com.example.gradingsystemservlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.EnrollmentDao;
import dao.EnrollmentDaoInterface;
import dao.StudentDaoInterface;
import dao.StudentInfoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Enrollment;
import model.StudentInfo;

@WebServlet(name = "ProfilePage", value = "/ProfilePage")
public class ProfilePage extends HttpServlet {
    private StudentDaoInterface studentInfo = new StudentInfoDao();
    private EnrollmentDaoInterface enrollmentDao = new EnrollmentDao();

    public ProfilePage() throws SQLException {
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {

            String ssn = (String) session.getAttribute("ssn");
            System.out.println("ssn: "+ ssn);
            if(!session.getAttribute("ssn").equals(request.getParameter("ssn"))){
                response.sendRedirect("Error?error=user-not-logged-in");
            }
            else {
                StudentInfo userInfo = null;
                try {
                    userInfo = studentInfo.getStudentInfo(ssn);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
                session.setAttribute("userInfo", userInfo);
                List<Enrollment> enrollments = null;
                try {
                    enrollments = enrollmentDao.getEnrollments(ssn);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
                session.setAttribute("enrollments", enrollments);
                System.out.println(enrollments);
                session.setAttribute("authenticated", true);
                request.setAttribute("ssn", ssn);
                request.setAttribute("userInfo", userInfo);
                getServletContext().getRequestDispatcher("/profile.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "please sign in first");
            getServletContext().getRequestDispatcher("/StudentLogin").forward(request, response);
        }
    }
}
