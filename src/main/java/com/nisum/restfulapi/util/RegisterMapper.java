package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.Phone;
import com.nisum.restfulapi.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterMapper {

    public static User getUserFromRequestRegister(RequestRegister requestRegister) {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .idUser(UUID.randomUUID().toString())
                .name(requestRegister.getName())
                .email(requestRegister.getEmail())
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(Boolean.TRUE)
                .build();
        if (requestRegister.getPhones() != null) {
            requestRegister.getPhones().forEach(phoneDTO -> user.addPhone(getPhoneFromPhoneDto(phoneDTO)));
        }
        return user;
    }

    private static Phone getPhoneFromPhoneDto(PhoneDTO phoneDTO) {
        return Phone.builder()
                .number(phoneDTO.getNumber())
                .cityCode(phoneDTO.getCitycode())
                .countryCode(phoneDTO.getCountrycode())
                .build();
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
