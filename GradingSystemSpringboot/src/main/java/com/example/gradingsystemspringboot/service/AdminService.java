package com.example.gradingsystemspringboot.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private SessionService sessionService;

    public AdminService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public boolean validateLogin(String username, String password){

        boolean isValid =  username.equals("Admin") && password.equals("Admin");
        if(isValid)
            sessionService.login("admin");
        return isValid;
    }
}
