package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;


    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User newUser){
        userService.saveNewUser(newUser);
        return newUser;
    }
}
