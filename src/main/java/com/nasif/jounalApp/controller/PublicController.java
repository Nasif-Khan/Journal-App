package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.Utils.JwtUtil;
import com.nasif.jounalApp.dto.LoginDTO;
import com.nasif.jounalApp.dto.SignupDTO;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.mapper.LoginDtoMapper;
import com.nasif.jounalApp.mapper.SignupDtoMapper;
import com.nasif.jounalApp.service.UserDetailsServiceImpl;
import com.nasif.jounalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name="1. Public APIs", description = "No authentication or authorization required. Checks if the api is working or not. User can login or signup from here.")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    @Operation(summary = "To check if the basic get request api works or not")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/signup")
    @Operation(summary = "To register on the journal app as a user. Execute it to get the jwt token to authenticate for other endpoints")

    public ResponseEntity<User> createUser(@RequestBody SignupDTO user){
        User newUser = SignupDtoMapper.toEntity(user);
        boolean created = userService.saveNewUser(newUser);
        if(created){
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/login")
    @Operation(summary = "To access the journal, and perform CRUD operation on the user or their journal entries")

    public ResponseEntity<String> login(@RequestBody LoginDTO currentUser){
        User user = LoginDtoMapper.toEntity(currentUser);
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while logging in", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
