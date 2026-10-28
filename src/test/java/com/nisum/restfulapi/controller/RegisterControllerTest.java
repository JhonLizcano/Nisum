package com.nisum.restfulapi.controller;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.service.RegisterService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterController Test Suite")
public class RegisterControllerTest {

    @InjectMocks
    RegisterController registerController;

    @Mock
    RegisterService registerService;

    @Test
    protected void createRegister() {
        RequestRegister requestRegister = RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .phones(List.of(PhoneDTO.builder()
                        .number("3001245778")
                        .citycode("1")
                        .countrycode("57")
                        .build()))
                .build();

        when(registerService.saveRegister(any(RequestRegister.class)))
                .thenReturn(ResponseRegister.builder()
                        .user(UserDto.builder()
                                .id("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                                .token("jwt-de-prueba")
                                .isactive(Boolean.TRUE)
                                .build())
                        .build());

        ResponseEntity<ResponseRegister> responseEntity = registerController.createRegister(requestRegister);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("6e8b6014-2c70-478d-9bc1-e50deb22f46b", responseEntity.getBody().getUser().getId());
        assertEquals("jwt-de-prueba", responseEntity.getBody().getUser().getToken());
        assertEquals(Boolean.TRUE, responseEntity.getBody().getUser().getIsactive());
    }

}
