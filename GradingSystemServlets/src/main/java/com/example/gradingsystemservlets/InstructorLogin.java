package com.example.gradingsystemservlets;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import dao.InstructorDaoInterface;
import dao.InstructorInfoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Instructor;

@WebServlet(name = "InstructorLogin", value = "/InstructorLogin")
public class InstructorLogin extends HttpServlet {
    private InstructorDaoInterface instructorInfoDao = new InstructorInfoDao();

    private Instructor instructor = new Instructor();
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        String hashedPassword = hashPassword(request.getParameter("password"));
        instructor.setIsn(request.getParameter("isn"));
        instructor.setPassword(hashedPassword);
        StringBuilder error = new StringBuilder();
        try {
            if (instructorInfoDao.validate(instructor)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("authenticatedInstructor", true);
                session.setAttribute("isn", instructor.getIsn());
                response.sendRedirect("InstructorProfilePage?isn=" + instructor.getIsn());
            } else {
                request.setAttribute("error", "Wrong number or password");
                getServletContext().getRequestDispatcher("/InstructorLogin.jsp").forward(request, response);
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

        if (session != null && session.getAttribute("authenticatedInstructor") != null && (boolean) session.getAttribute("authenticatedInstructor")) {

            response.sendRedirect("InstructorProfilePage?isn=" + instructor.getIsn());
        }
        else
            getServletContext().getRequestDispatcher("/InstructorLogin.jsp").forward(request, response);

    }
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
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