package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.api.response.QuoteResponse;
import com.nasif.jounalApp.api.response.WeatherResponse;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.repository.UserRepository;
import com.nasif.jounalApp.service.QuoteService;
import com.nasif.jounalApp.service.UserService;
import com.nasif.jounalApp.service.WeatherService;
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
@RequestMapping("/user")
@Tag(name="2. User APIs", description = "Get quotes, weather info for the user & update or delete user.")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private QuoteService quoteService;


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
//        We need credentials to update the user, as there is no path parameter
//        This comes from SecurityContextHolder
//        SecurityContextHolder have the context i.e. the username and password of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDB = userService.findByUserName(username);
        userInDB.setUserName(user.getUserName());
        userInDB.setPassword(user.getPassword());
        userService.saveNewUser(userInDB);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/greet")
    @Operation(summary = "greet the user and gets the current weather of the user's location")
    public ResponseEntity<?> greetUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String greeting = "";
        String weatherDescription = "";
        String user = authentication.getName();
        WeatherResponse weatherResponse = weatherService.getWeather("Hyderabad");
        if(weatherResponse != null){
            int feelslike = weatherResponse.getCurrent().getFeelslike();
            greeting = "Hi " + user + ", weather feels like, " + feelslike + "°C";
            weatherDescription = weatherResponse.getCurrent().getWeatherDescriptions().get(0);
        }
        return new ResponseEntity<>(greeting + "\n" + weatherDescription , HttpStatus.OK);
    }

    @GetMapping("/quote")
    @Operation(summary = "get user a random quote")
    public ResponseEntity<?> quoteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String quoteMessage = "";
        List<QuoteResponse> quoteResponse = quoteService.getQuote();
        if (quoteResponse != null && !quoteResponse.isEmpty()) {
            QuoteResponse quoteObj = quoteResponse.get(0); // get the first quote

            String quote = quoteObj.getQuote();
            String author = quoteObj.getAuthor();
            String category = quoteObj.getCategory();

            quoteMessage = "Hi " + user + "\nHere is a '" + category + "' category quote by " + author + "\n" + quote;

        }
        return new ResponseEntity<>(quoteMessage , HttpStatus.OK);

    }
}