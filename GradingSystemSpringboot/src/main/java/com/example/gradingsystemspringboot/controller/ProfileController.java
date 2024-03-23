package com.example.gradingsystemspringboot.controller;


import com.example.gradingsystemspringboot.model.Course;
import com.example.gradingsystemspringboot.model.Enrollment;
import com.example.gradingsystemspringboot.service.EnrollmentService;
import com.example.gradingsystemspringboot.service.SessionService;
import com.example.gradingsystemspringboot.service.StudentService;
import com.example.gradingsystemspringboot.service.TeachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {

    private final EnrollmentService enrollmentService;
    private final SessionService sessionService;
    private final TeachingService teachingService;


    @Autowired
    public ProfileController(EnrollmentService enrollmentService, SessionService sessionService, TeachingService teachingService) {
        this.enrollmentService = enrollmentService;

        this.sessionService = sessionService;
        this.teachingService = teachingService;
    }

    @GetMapping("/student/profile/{ssn}")
    public String showStudentProfile(Model model, @PathVariable String ssn, RedirectAttributes redirectAttributes) {
        if(sessionService.isLoggedIn(ssn)) {
            System.out.println("Hello anybody here?" + ssn);
            List<Enrollment> enrollments;
            try {
                enrollments = enrollmentService.getEnrollments(ssn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            model.addAttribute("enrollments", enrollments);
            return "student-profile";
        }
        else{
             redirectAttributes.addFlashAttribute("error", "Login first please");
             return "redirect:/login/student";
        }
    }
    @GetMapping("/instructor/profile/{isn}")
    public String showInstructorProfile(Model model, @PathVariable String isn, RedirectAttributes redirectAttributes){
        if(sessionService.isLoggedIn(isn)){
            System.out.println("Hello");
            List<Course> courses;
            try{
                courses = teachingService.getCourses(isn);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            model.addAttribute("courses", courses);
            return "instructor-profile";

        }
        redirectAttributes.addFlashAttribute("error", "Login first please");
        return "redirect:/login/instructor";

    }

    @GetMapping("/admin/profile")
    public String showAdminProfile(Model model, RedirectAttributes redirectAttributes){
        if(sessionService.isLoggedIn("admin")){
            return "admin-profile";
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Login first please");
            return "redirect:/login/admin";
        }
    }



}
