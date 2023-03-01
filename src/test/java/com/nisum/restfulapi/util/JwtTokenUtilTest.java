package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenUtil Test Suite")
public class JwtTokenUtilTest {

    @InjectMocks
    JwtTokenUtil jwtTokenUtil;

    @Test
    protected void generateToken(){
        User user = User.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                .build();

        String token = jwtTokenUtil.generateToken(user);

        assertNotNull(token);
    }
}
