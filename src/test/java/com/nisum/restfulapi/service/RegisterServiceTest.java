package com.nisum.restfulapi.service;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.Phone;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.repository.PhoneRepository;
import com.nisum.restfulapi.repository.RegisterRepository;
import com.nisum.restfulapi.service.impl.RegisterServiceImpl;
import com.nisum.restfulapi.util.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterService Test Suite")
public class RegisterServiceTest {

    @InjectMocks
    RegisterServiceImpl registerService;

    @Mock
    RegisterRepository registerRepository;

    @Mock
    PhoneRepository phoneRepository;

    @Test
    protected void saveRegister() {
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

        Mockito.when(registerRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(registerRepository.save(Mockito.any(User.class)))
                .thenReturn(User.builder()
                        .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                        .token("eyJhbGciOiJub25lIn0.eyJuYW1lIjoiSnVhbiBSb2RyaWd1ZXoiLCJlbWFpbCI6Im" +
                                "p1YW5Acm9kcmlndWV6Lm9yZyIsInN1YiI6Ikp1YW4gUm9kcmlndWV6IiwianRpIjo" +
                                "iODU1YzI5MDctMGU1Zi00MzY2LWExNzktOGI1YmVmZWY4YzRkIiwiaWF0IjoxNjc3" +
                                "NjExMDY5LCJleHAiOjE2Nzc2MTE5Njl9.")
                        .isActive(Boolean.TRUE)
                        .build());
        Mockito.when(phoneRepository.save(Mockito.any(Phone.class)))
                .thenReturn(new Phone());

        ResponseRegister responseRegister = registerService.saveRegister(requestRegister);

        assertNotNull(responseRegister);
        assertNotNull(responseRegister.getUser());
        assertEquals(responseRegister.getUser().getId(), responseRegisterExpected.getUser().getId());
        assertEquals(responseRegister.getUser().getToken(), responseRegisterExpected.getUser().getToken());
        assertEquals(responseRegister.getUser().getIsactive(),
                responseRegisterExpected.getUser().getIsactive());
        assertNull(responseRegister.getMensaje());
    }

    @Test
    protected void saveRegisterPasswordInvalid() {
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(PhoneDTO.builder()
                .number(3001245778L)
                .citycode(1)
                .countrycode(57)
                .build());
        RequestRegister requestRegister = RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter3$%&")
                .phones(phones)
                .build();

        ResponseRegister responseRegisterExpected = ResponseRegister.builder()
                .mensaje(Constants.REGEX_PASSWORD)
                .build();

        ResponseRegister responseRegister = registerService.saveRegister(requestRegister);

        assertNotNull(responseRegister);
        assertNull(responseRegister.getUser());
        assertNotNull(responseRegister.getMensaje());
        assertEquals(responseRegister.getMensaje(), Constants.REGEX_PASSWORD);
    }

    @Test
    protected void saveRegisterEmailExist() {
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

        Mockito.when(registerRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(User.builder()
                        .name("Jhon Parra")
                        .email("Jhon@parra.org")
                        .password("Hunter3$%&")
                        .build()));

        ResponseRegister responseRegister = registerService.saveRegister(requestRegister);

        assertNotNull(responseRegister);
        assertNull(responseRegister.getUser());
        assertNotNull(responseRegister.getMensaje());
        assertEquals(responseRegister.getMensaje(), Constants.EXIST_MAIL);
    }
}
