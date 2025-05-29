package com.nasif.jounalApp.mapper;

import com.nasif.jounalApp.dto.SignupDTO;
import com.nasif.jounalApp.entity.User;

public class SignupDtoMapper {
    public static User toEntity(SignupDTO dto) {
        return User.builder()
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .sentimentAnalysis(dto.isSentimentAnalysis())
                .build();
    }
}
