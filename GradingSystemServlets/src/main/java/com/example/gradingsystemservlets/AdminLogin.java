package com.example.gradingsystemservlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AdminLogin", value = "/AdminLogin")
public class AdminLogin extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticatedAdmin") != null && (boolean) session.getAttribute("authenticatedAdmin")) {

            response.sendRedirect("AdminPage");
        }
        else
            getServletContext().getRequestDispatcher("/AdminLogin.jsp").forward(request, response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username.equals("Admin") && password.equals("Admin")){
            HttpSession session = request.getSession(true);
            session.setAttribute("authenticatedAdmin", true);
            response.sendRedirect("AdminPage");

        }
        else{
            request.setAttribute("error", "Wrong number or password");
            getServletContext().getRequestDispatcher("/AdminLogin.jsp").forward(request, response);
        }
    }
}
