package com.example.gradingsystemspringboot.service;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {

    private final Map<String, Boolean> loggedInUsers = new HashMap<>();

    public void login(String ssn) {
        loggedInUsers.put(ssn, true);
    }

    public void logout(String ssn) {
        loggedInUsers.remove(ssn);
    }

    public boolean isLoggedIn(String ssn) {
        return loggedInUsers.containsKey(ssn) && loggedInUsers.get(ssn);
    }
}

