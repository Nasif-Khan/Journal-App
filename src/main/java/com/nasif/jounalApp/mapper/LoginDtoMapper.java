package com.nasif.jounalApp.mapper;

import com.nasif.jounalApp.dto.LoginDTO;
import com.nasif.jounalApp.entity.User;

public class LoginDtoMapper {
    public static User toEntity(LoginDTO dto) {
        return User.builder()
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .build();
    }
}

