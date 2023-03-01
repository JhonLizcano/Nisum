package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterMapper Test Suite")
public class RegisterMapperTest {

    @InjectMocks
    RegisterMapper registerMapper;

    @Test
    protected void getUserFromRequestRegister(){
        RequestRegister requestRegister = RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .build();

        User userExpected = User.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .build();

        User user = registerMapper.getUserFromRequestRegister(requestRegister);

        assertNotNull(user);
        assertEquals(user.getName(), userExpected.getName());
        assertEquals(user.getEmail(), userExpected.getEmail());
        assertEquals(user.getPassword(), userExpected.getPassword());
        assertTrue(user.getIsActive());
    }

    @Test
    protected void getUserDtoFromUser(){
        User user = User.builder()
                .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                .token("eyJhbGciOiJub25lIn0.eyJuYW1lIjoiSnVhbiBSb2RyaWd1ZXoiLCJlbWFpbCI6Im" +
                        "p1YW5Acm9kcmlndWV6Lm9yZyIsInN1YiI6Ikp1YW4gUm9kcmlndWV6IiwianRpIjo" +
                        "iODU1YzI5MDctMGU1Zi00MzY2LWExNzktOGI1YmVmZWY4YzRkIiwiaWF0IjoxNjc3" +
                        "NjExMDY5LCJleHAiOjE2Nzc2MTE5Njl9.")
                .isActive(Boolean.TRUE)
                .build();

        UserDto userDtoExpected = UserDto.builder()
                .id("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                .token("eyJhbGciOiJub25lIn0.eyJuYW1lIjoiSnVhbiBSb2RyaWd1ZXoiLCJlbWFpbCI6Im" +
                        "p1YW5Acm9kcmlndWV6Lm9yZyIsInN1YiI6Ikp1YW4gUm9kcmlndWV6IiwianRpIjo" +
                        "iODU1YzI5MDctMGU1Zi00MzY2LWExNzktOGI1YmVmZWY4YzRkIiwiaWF0IjoxNjc3" +
                        "NjExMDY5LCJleHAiOjE2Nzc2MTE5Njl9.")
                .isactive(Boolean.TRUE)
                .build();

        UserDto userDto = registerMapper.getUserDtoFromUser(user);

        assertNotNull(user);
        assertEquals(user.getIdUser(), userDtoExpected.getId());
        assertEquals(user.getToken(), userDtoExpected.getToken());
        assertEquals(user.getIsActive(), userDtoExpected.getIsactive());
    }
}
