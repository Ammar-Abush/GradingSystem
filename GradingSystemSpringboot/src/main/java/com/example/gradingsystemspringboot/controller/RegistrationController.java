package com.example.gradingsystemspringboot.controller;


import com.example.gradingsystemspringboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    @Autowired
    private StudentService studentService;
    @PostMapping("/admin/profile")
    public String registerStudent(@RequestParam String ssn,
                                  @RequestParam String firstName,
                                  @RequestParam String mi,
                                  @RequestParam  String lastName,
                                  @RequestParam java.sql.Date birthDate,String street,String phone, String zipcode,String deptId, String password, Model model){
        if (ssn == null || ssn.length() != 9 || !ssn.matches("[0-9]+")) {
            model.addAttribute("Message", "Invalid ssn");
            return "error";
        }

        if (firstName == null || firstName.isEmpty()) {
            model.addAttribute("Message", "Invalid firstName");
            return "error";
        }

        if (mi == null || mi.length() != 1) {
            model.addAttribute("Message", "Invalid Middle Initial");
            return "error";
        }

        if (lastName == null || lastName.isEmpty()) {
            model.addAttribute("Message", "Last Name is required");
            return "error";
        }
        if(phone.length() != 11 || !phone.matches("[0-9]+")){
            model.addAttribute("Message", "Invalid Phone");
            return "error";
        }

        try{
            System.out.println(ssn);
            studentService.RegisterStudent(ssn, firstName,mi, lastName,birthDate, street,phone, zipcode, deptId, password);
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("Message", "An error occurred please try again");
            return "error";
        }
        return "redirect:/admin/success";

    }
}
