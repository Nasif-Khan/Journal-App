package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.cache.AppCache;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.repository.UserRepositoryImpl;
import com.nasif.jounalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody User user) {
        userService.saveAdmin(user);
    }

    @GetMapping("/refresh-app-cache")
    public void refreshAppCache() {
        appCache.init();
    }

    @GetMapping("/check-email")
    public ResponseEntity<List<User>> checkEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersForSA = userRepositoryImpl.getUsersForSA();
        return new ResponseEntity<>(usersForSA, HttpStatus.OK);
    }
}
