package com.nasif.jounalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
@Tag(name="Home APIs", description = "Redirects to the swagger page")

public class HomeController {
    @GetMapping
    public String redirectToSwagger(){
        System.out.println("redirected to swagger");
        log.info("Redirected to swagger");
        return "redirect:/swagger-ui/index.html";
    }
}
