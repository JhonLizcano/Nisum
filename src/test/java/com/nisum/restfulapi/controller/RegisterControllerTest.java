package com.nisum.restfulapi.controller;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterController Test Suite")
public class RegisterControllerTest {

    @InjectMocks
    RegisterController registerController;

    @Mock
    RegisterService registerService;

    @Test
    protected void createRegister() throws Exception {
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(PhoneDTO.builder()
                .number(3001245778L)
                .citycode(1)
                .countrycode(57)
                .build());
        RequestRegister requestRegister = RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .phones(phones)
                .build();

        ResponseRegister responseRegisterExpected = ResponseRegister.builder()
                .user(UserDto.builder()
                        .id("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                        .token("eyJhbGciOiJub25lIn0.eyJuYW1lIjoiSnVhbiBSb2RyaWd1ZXoiLCJlbWFpbCI6Im" +
                                "p1YW5Acm9kcmlndWV6Lm9yZyIsInN1YiI6Ikp1YW4gUm9kcmlndWV6IiwianRpIjo" +
                                "iODU1YzI5MDctMGU1Zi00MzY2LWExNzktOGI1YmVmZWY4YzRkIiwiaWF0IjoxNjc3" +
                                "NjExMDY5LCJleHAiOjE2Nzc2MTE5Njl9.")
                        .isactive(Boolean.TRUE)
                        .build())
                .build();

        Mockito.when(registerService.saveRegister(Mockito.any(RequestRegister.class)))
                .thenReturn(ResponseRegister.builder()
                        .user(UserDto.builder()
                                .id("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                                .token("eyJhbGciOiJub25lIn0.eyJuYW1lIjoiSnVhbiBSb2RyaWd1ZXoiLCJlbWFpbCI6Im" +
                                        "p1YW5Acm9kcmlndWV6Lm9yZyIsInN1YiI6Ikp1YW4gUm9kcmlndWV6IiwianRpIjo" +
                                        "iODU1YzI5MDctMGU1Zi00MzY2LWExNzktOGI1YmVmZWY4YzRkIiwiaWF0IjoxNjc3" +
                                        "NjExMDY5LCJleHAiOjE2Nzc2MTE5Njl9.")
                                .isactive(Boolean.TRUE)
                                .build())
                        .build());

        ResponseEntity<ResponseRegister> responseEntity = registerController.createRegister(requestRegister);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody().getUser());
        assertEquals(responseEntity.getBody().getUser().getId(), responseRegisterExpected.getUser().getId());
        assertEquals(responseEntity.getBody().getUser().getToken(), responseRegisterExpected.getUser().getToken());
        assertEquals(responseEntity.getBody().getUser().getIsactive(),
                responseRegisterExpected.getUser().getIsactive());
        assertNull(responseEntity.getBody().getMensaje());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    protected void createRegisterEmailExist() throws Exception {
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(PhoneDTO.builder()
                .number(3001245778L)
                .citycode(1)
                .countrycode(57)
                .build());
        RequestRegister requestRegister = RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .phones(phones)
                .build();

        ResponseRegister responseRegisterExpected = ResponseRegister.builder()
                .mensaje(Constants.EXIST_MAIL)
                .build();

        Mockito.when(registerService.saveRegister(Mockito.any(RequestRegister.class)))
                .thenReturn(ResponseRegister.builder()
                        .mensaje(Constants.EXIST_MAIL)
                        .build());

        ResponseEntity<ResponseRegister> responseEntity = registerController.createRegister(requestRegister);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody().getUser());
        assertNotNull(responseEntity.getBody().getMensaje());
        assertEquals(responseEntity.getBody().getMensaje(), Constants.EXIST_MAIL);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
