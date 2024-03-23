package com.example.gradingsystemservlets;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import dao.StudentDaoInterface;
import dao.StudentInfoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import model.StudentInfo;

@WebServlet(name = "StudentLogin", value = "/StudentLogin")
public class StudentLogin extends HttpServlet {
    private StudentDaoInterface studentDaoInterface = new StudentInfoDao();
    private StudentInfo student = new StudentInfo();
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        String hashedPassword = hashPassword(request.getParameter("password"));
        student.setSsn(request.getParameter("ssn"));
        student.setPassword(hashedPassword);
        StringBuilder error = new StringBuilder();
        try {
            if (studentDaoInterface.validate(student)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("authenticated", true);
                session.setAttribute("ssn", student.getSsn());
                response.sendRedirect("ProfilePage?ssn=" + student.getSsn());
            } else {
                request.setAttribute("error", "Wrong number or password");
                getServletContext().getRequestDispatcher("/StudentLogin.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {

            response.sendRedirect("ProfilePage?ssn=" + student.getSsn());
        }
        else
            getServletContext().getRequestDispatcher("/StudentLogin.jsp").forward(request, response);

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