package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.Phone;
import com.nisum.restfulapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RegisterMapper Test Suite")
class RegisterMapperTest {

    private RequestRegister buildRequest(List<PhoneDTO> phones) {
        return RequestRegister.builder()
                .name("Jhon Parra")
                .email("Jhon@parra.org")
                .password("Hunter")
                .phones(phones)
                .build();
    }

    @Test
    @DisplayName("Crea un usuario activo con id UUID, las tres fechas iguales y los teléfonos enlazados")
    void getUserFromRequestRegister() {
        RequestRegister request = buildRequest(List.of(PhoneDTO.builder()
                .number("3001245778")
                .citycode("1")
                .countrycode("57")
                .build()));

        User user = RegisterMapper.getUserFromRequestRegister(request);

        assertEquals("Jhon Parra", user.getName());
        assertEquals("Jhon@parra.org", user.getEmail());
        assertTrue(user.getIsActive());
        assertDoesNotThrow(() -> UUID.fromString(user.getIdUser()));

        assertNotNull(user.getCreated());
        assertEquals(user.getCreated(), user.getModified());
        assertEquals(user.getCreated(), user.getLastLogin());

        // La clave con hash y el token los asigna el servicio, no el mapper
        assertNull(user.getPassword());
        assertNull(user.getToken());

        assertEquals(1, user.getPhones().size());
        Phone phone = user.getPhones().get(0);
        assertEquals("3001245778", phone.getNumber());
        assertEquals("1", phone.getCityCode());
        assertEquals("57", phone.getCountryCode());
        assertSame(user, phone.getUser());
    }

    @Test
    @DisplayName("Sin teléfonos en la petición, el usuario queda con lista vacía")
    void getUserFromRequestRegisterWithoutPhones() {
        User user = RegisterMapper.getUserFromRequestRegister(buildRequest(null));

        assertTrue(user.getPhones().isEmpty());
    }

    @Test
    @DisplayName("El DTO copia id, fechas, token y estado del usuario")
    void getUserDtoFromUser() {
        LocalDateTime created = LocalDateTime.of(2026, 10, 19, 22, 40, 43);
        LocalDateTime modified = LocalDateTime.of(2026, 10, 20, 8, 0, 0);
        LocalDateTime lastLogin = LocalDateTime.of(2026, 10, 21, 9, 30, 0);
        User user = User.builder()
                .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                .created(created)
                .modified(modified)
                .lastLogin(lastLogin)
                .token("jwt-de-prueba")
                .isActive(Boolean.TRUE)
                .build();

        UserDto userDto = RegisterMapper.getUserDtoFromUser(user);

        assertEquals("6e8b6014-2c70-478d-9bc1-e50deb22f46b", userDto.getId());
        assertEquals(created, userDto.getCreated());
        assertEquals(modified, userDto.getModified());
        assertEquals(lastLogin, userDto.getLast_login());
        assertEquals("jwt-de-prueba", userDto.getToken());
        assertEquals(Boolean.TRUE, userDto.getIsactive());
    }

}
