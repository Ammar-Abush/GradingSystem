package com.example.gradingsystemspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperationsController {
    @GetMapping("/instructor/success")
    public String showMessage(Model model){
        model.addAttribute("Message", "Success!");
        return "success";
    }
    @GetMapping("/admin/success")
    public String showMessageAdmin(Model model){
        model.addAttribute("Message", "Success!");
        return "success";
    }
}
