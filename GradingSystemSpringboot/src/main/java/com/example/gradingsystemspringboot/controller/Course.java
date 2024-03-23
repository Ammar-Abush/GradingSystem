package com.example.gradingsystemspringboot.controller;


import com.example.gradingsystemspringboot.model.StudentInfo;
import com.example.gradingsystemspringboot.service.SessionService;
import com.example.gradingsystemspringboot.service.TeachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class Course {
    private final TeachingService teachingService;
    private final SessionService sessionService;

    @Autowired
    public Course(TeachingService teachingService, SessionService sessionService) {
        this.teachingService = teachingService;
        this.sessionService = sessionService;
    }

    @GetMapping("/instructor/course")
    public String showCourses(Model model, @RequestParam String courseId, @RequestParam String isn){
        if(sessionService.isLoggedIn(isn)) {
            List<StudentInfo> students;
            try {
                students = teachingService.getStudents(isn, courseId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            model.addAttribute("students", students);
            return "course";
        }
        else{
            return "redirect:/login/instructor";
        }
    }
}
