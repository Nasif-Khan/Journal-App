package com.nasif.jounalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    private String userName;
    private String password;
    private String email;
    private boolean sentimentAnalysis;
}
