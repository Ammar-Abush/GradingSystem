package com.example.gradingsystemspringboot.controller;

import com.example.gradingsystemspringboot.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MarksController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/instructor/course")
    public String assignMarks(@RequestParam String courseId,
                              @RequestParam String isn,
                              @RequestParam String studentSsn,
                              @RequestParam  int mark,
                              @RequestParam String grade, Model model) {
        String[] parts = isn.split(",");
        String issn = parts[1];
        String[] partss = courseId.split(",");
        String courseID = partss[1];


        try {
            enrollmentService.assignMarks(issn, courseID, studentSsn, mark, grade);
        } catch (ClassNotFoundException e) {
            model.addAttribute("Message", "An error occurred please try again");
            return "error";

        }

        return "redirect:/instructor/success";
    }
}

