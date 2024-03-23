package com.example.gradingsystemservlets;

import dao.EnrollmentDao;
import dao.EnrollmentDaoInterface;
import dao.TeachingDao;
import dao.TeachingDaoInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Enrollment;
import model.StudentInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AssignMarks", value = "/AssignMarks")
public class AssignMarks extends HttpServlet {
    private EnrollmentDaoInterface enrollmentDao = new EnrollmentDao();
    private TeachingDaoInterface teachingDao = new TeachingDao();
    public AssignMarks() throws SQLException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("authenticatedInstructor") != null && (boolean) session.getAttribute("authenticatedInstructor") && request.getParameter("isn").equals(session.getAttribute("isn"))){
            List<StudentInfo> students = null;
            try {
               students = teachingDao.getStudents((String) session.getAttribute("isn"), request.getParameter("courseId"));

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("students", students);
            getServletContext().getRequestDispatcher("/AssignMarks.jsp").forward(request, response);
        }
        else{
            request.setAttribute("error", "please sign in first");
            getServletContext().getRequestDispatcher("/InstructorLogin").forward(request, response);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("authenticatedInstructor") != null && (boolean) session.getAttribute("authenticatedInstructor")) {
            String isn = (String) session.getAttribute("isn");
            String courseId = request.getParameter("courseId");
            String studentSsn = request.getParameter("studentSsn");
            String markParam = request.getParameter("mark");
            String grade = request.getParameter("grade");


            if (courseId == null || courseId.isEmpty() || studentSsn == null || studentSsn.isEmpty() || markParam == null || markParam.isEmpty() || grade == null || grade.isEmpty()) {
                response.sendRedirect("Error?error=All fields must be filled out");
                return;
            }


            try {
                int mark = Integer.parseInt(markParam);
                if (mark < 0 || mark > 100){
                    response.sendRedirect("Error?error=Invalid 'mark' value. Please enter a valid integer");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("Error?error=Invalid 'mark' value. Please enter a valid integer");
                return;
            }



            try {
                enrollmentDao.assignMarks(isn, courseId, studentSsn, Integer.parseInt(markParam), grade);
                response.sendRedirect("mark_assign_confirmation.jsp");
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
                response.sendRedirect("Error?error=An unexpected error occurred while processing the request");
            }
        } else {

            response.sendRedirect("login.jsp");
        }
    }

}
