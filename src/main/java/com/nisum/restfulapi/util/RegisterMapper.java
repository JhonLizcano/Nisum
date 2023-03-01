package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.User;

import java.util.Date;
import java.util.UUID;

public class RegisterMapper {

    public static User getUserFromRequestRegister(RequestRegister requestRegister) {
        User user = User.builder()
                .idUser(UUID.randomUUID().toString())
                .name(requestRegister.getName())
                .email(requestRegister.getEmail())
                .password(requestRegister.getPassword())
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .isActive(Boolean.TRUE)
                .build();
        user.setToken(JwtTokenUtil.generateToken(user));
        return user;
    }

    public static UserDto getUserDtoFromUser(User user) {
        return UserDto.builder()
                .id(user.getIdUser())
                .created(user.getCreated())
                .modified(user.getModified())
                .last_login(user.getLastLogin())
                .token(user.getToken())
                .isactive(user.getIsActive())
                .build();
    }
}
