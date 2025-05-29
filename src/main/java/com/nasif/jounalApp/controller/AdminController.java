package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.cache.AppCache;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.repository.UserRepositoryImpl;
import com.nasif.jounalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name="4. Admin APIs", description = "Cannot be accessed by you, only admin can access. \nGet all registered users and email of users who opted in for mail. Create a new admin user and refreshes the cache. ")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @GetMapping("/all-users")
    @Operation(summary = "Retrieves all the registered users in the journal application ")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "Gives the user the ADMIN role")
    public void createUser(@RequestBody User user) {
        userService.saveAdmin(user);
    }

    @GetMapping("/refresh-app-cache")
    @Operation(summary = "Cleans the cache memory and updates with fresh data")
    public void refreshAppCache() {
        appCache.init();
    }

    @GetMapping("/check-email")
    @Operation(summary = "Gets all the users who opted in for mail subscription")
    public ResponseEntity<List<User>> checkEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersForSA = userRepositoryImpl.getUsersForSA();
        return new ResponseEntity<>(usersForSA, HttpStatus.OK);
    }
}
