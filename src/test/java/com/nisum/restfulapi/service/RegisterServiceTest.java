package com.nisum.restfulapi.service;

import com.nisum.restfulapi.exceptions.EmailAlreadyExistsException;
import com.nisum.restfulapi.exceptions.InvalidPasswordException;
import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.repository.UserRepository;
import com.nisum.restfulapi.security.TokenProvider;
import com.nisum.restfulapi.service.impl.RegisterServiceImpl;
import com.nisum.restfulapi.util.Constants;
import com.nisum.restfulapi.validation.PasswordPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterService Test Suite")
public class RegisterServiceTest {

    @InjectMocks
    RegisterServiceImpl registerService;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    PasswordPolicy passwordPolicy;

    private RequestRegister buildRequest(String password) {
        return RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password(password)
                .phones(List.of(PhoneDTO.builder().number("3001245778").citycode("1").countrycode("57").build()))
                .build();
    }

    @Test
    protected void saveRegister() {
        RequestRegister requestRegister = buildRequest("Hunter");

        when(passwordPolicy.isValid("Hunter")).thenReturn(true);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(tokenProvider.generateToken(any(User.class))).thenReturn("jwt-de-prueba");
        when(passwordEncoder.encode("Hunter")).thenReturn("hash-de-prueba");
        when(userRepository.save(any(User.class)))
                .thenReturn(User.builder()
                        .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                        .token("jwt-de-prueba")
                        .isActive(Boolean.TRUE)
                        .build());

        UserDto userDto = registerService.saveRegister(requestRegister);

        assertNotNull(userDto);
        assertEquals("6e8b6014-2c70-478d-9bc1-e50deb22f46b", userDto.getId());
        assertEquals("jwt-de-prueba", userDto.getToken());
        assertEquals(Boolean.TRUE, userDto.getIsactive());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("jwt-de-prueba", savedUser.getToken());
        assertEquals("hash-de-prueba", savedUser.getPassword());
        assertEquals(1, savedUser.getPhones().size());
        assertSame(savedUser, savedUser.getPhones().get(0).getUser());
    }

    @Test
    protected void saveRegisterPasswordInvalid() {
        RequestRegister requestRegister = buildRequest("Hunter3$%&");

        when(passwordPolicy.isValid("Hunter3$%&")).thenReturn(false);

        InvalidPasswordException ex = assertThrows(InvalidPasswordException.class,
                () -> registerService.saveRegister(requestRegister));

        assertEquals(Constants.REGEX_PASSWORD, ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    protected void saveRegisterEmailExist() {
        RequestRegister requestRegister = buildRequest("Hunter");

        when(passwordPolicy.isValid("Hunter")).thenReturn(true);
        when(userRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.of(User.builder()
                        .name("Jhon Parra")
                        .email("Jhon@parra.org")
                        .password("hash-existente")
                        .build()));

        EmailAlreadyExistsException ex = assertThrows(EmailAlreadyExistsException.class,
                () -> registerService.saveRegister(requestRegister));

        assertEquals(Constants.EXIST_MAIL, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

}
