package com.example.gradingsystemspringboot.controller;

import com.example.gradingsystemspringboot.model.Enrollment;
import com.example.gradingsystemspringboot.model.Instructor;
import com.example.gradingsystemspringboot.model.StudentInfo;
import com.example.gradingsystemspringboot.service.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private StudentService studentService;
    private EnrollmentService enrollmentService;
    private SessionService sessionService;
    private InstructorService instructorService;
    private AdminService adminService;

    public LoginController(SessionService sessionService, InstructorService instructorService, AdminService adminService) {
        this.sessionService = sessionService;
        this.instructorService = instructorService;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/login/student")
    public String showStudentLoginForm() {
        return "student-login";
    }
    @GetMapping("login/instructor")
    public String showInstructorLoginForm(){
        return "instructor-login";
    }
    @GetMapping("/login/admin")
        public String showAdminLoginFrom(){
            return "admin-login";
    }


    @PostMapping("/login/student")
    public String processStudentLogin(@RequestParam String ssn, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
        StudentInfo student = new StudentInfo();
        student.setSsn(ssn);
        student.setPassword(password);
        if (studentService.validate(student)) {
            redirectAttributes.addFlashAttribute("student", studentService.getStudentInfo(ssn));
            return "redirect:/student/profile/" + ssn;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
            return "redirect:/login/student";
        }
    }
    @PostMapping("/login/instructor")
    public String processInstructorLogin(@RequestParam String isn, @RequestParam String password, Model model, RedirectAttributes redirectAttributes){
        Instructor instructor = new Instructor();
        instructor.setIsn(isn);
        instructor.setPassword(password);
        if(instructorService.validate(instructor)){
            redirectAttributes.addFlashAttribute("instructor", instructorService.getInstructorInfo(isn));
            return "redirect:/instructor/profile/" + isn;
        }

        else{
            System.out.println("Invalid credentials. Please try again.");
            redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
            return "redirect:/login/instructor";
        }
    }
    @PostMapping("/login/admin")
    public String ProcessAdminLogin(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes redirectAttributes){
        if(adminService.validateLogin(username, password)){
            return "redirect:/admin/profile";
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
            return "redirect:/login/admin";
        }
    }
    @GetMapping("/student/logout")
    public String logout(@RequestParam String ssn) {
        System.out.println("LOLOLOLO" + ssn);
        sessionService.logout(ssn);
        return "redirect:/login/student";

    }
    @GetMapping("/instructor/logout")
    public String Instructorlogout(@RequestParam String isn) {
        System.out.println("LOLOLOLO" + isn);
        sessionService.logout(isn);
        return "redirect:/login/instructor";

    }

    @GetMapping("/admin/logout")
    public String AdminLogout(){
        sessionService.logout("admin");
        return "redirect:/login/admin";
    }


}

