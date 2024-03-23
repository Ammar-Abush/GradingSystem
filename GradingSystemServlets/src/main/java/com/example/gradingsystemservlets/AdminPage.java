package com.example.gradingsystemservlets;

import dao.StudentDaoInterface;
import dao.StudentInfoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

@WebServlet(name = "AdminPage", value = "/AdminPage")
public class AdminPage extends HttpServlet {
    private StudentDaoInterface studentInfo = new StudentInfoDao();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticatedAdmin") != null && (boolean) session.getAttribute("authenticatedAdmin")) {
            getServletContext().getRequestDispatcher("/AdminProfile.jsp").forward(request, response);
        }
        else{
            request.setAttribute("error", "please sign in first");
            getServletContext().getRequestDispatcher("/AdminLogin").forward(request, response);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ssn = request.getParameter("ssn");
        String firstName = request.getParameter("firstName");
        String mi = request.getParameter("mi");
        String lastName = request.getParameter("lastName");
        String birthDateStr = request.getParameter("birthDate");
        String street = request.getParameter("street");
        String phone = request.getParameter("phone");
        String zipcode = request.getParameter("zipcode");
        String deptId = request.getParameter("deptId");
        String password = request.getParameter("password");

        if (ssn == null || ssn.length() != 9 || !ssn.matches("[0-9]+")) {
            response.sendRedirect("Error?error=Invalid SSN");
            return;
        }

        if (firstName == null || firstName.isEmpty()) {
            response.sendRedirect("Error?error=First Name is required");
            return;
        }

        if (mi == null || mi.length() != 1) {
            response.sendRedirect("Error?error=Invalid Middle Initial");
            return;
        }

        if (lastName == null || lastName.isEmpty()) {
            response.sendRedirect("Error?error=Last Name is required");
            return;
        }




        String passwordHashed = hashPassword(password);


        java.sql.Date birthDate = null;
        try {
            birthDate = java.sql.Date.valueOf(birthDateStr);
        } catch (IllegalArgumentException e) {
            response.sendRedirect("Error?error=Invalid Birth Date");
            return;
        }

        try {

            studentInfo.registerStudent(ssn, firstName, mi, lastName, birthDate, street, phone, zipcode, deptId, passwordHashed);
            response.sendRedirect("registration_success.jsp");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Log the exception or handle it as needed
            response.sendRedirect("Error?error=Error");
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
            // Handle the exception appropriately
            return null;
        }
    }
}
