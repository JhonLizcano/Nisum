package com.nisum.restfulapi;

import com.jayway.jsonpath.JsonPath;
import com.nisum.restfulapi.model.Phone;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.repository.UserRepository;
import com.nisum.restfulapi.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Levanta la aplicación completa (validador, Jackson, base H2, BCrypt y JWT reales)
 * y la prueba por HTTP. @Transactional deshace los datos al terminar cada test.
 */
@SpringBootTest
@Transactional
@DisplayName("Registro de usuarios: integración de punta a punta")
class RegisterIntegrationTest {

    private static final String URL = "/register/create";
    private static final String JWT_FORMAT = "[\\w-]+\\.[\\w-]+\\.[\\w-]+";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static String uniqueEmail() {
        return "user-" + UUID.randomUUID() + "@example.com";
    }

    private static String bodyWith(String email, String password) {
        return """
                {
                  "name": "Juan Rodriguez",
                  "email": "%s",
                  "password": "%s",
                  "phones": [
                    {"number": "1234567", "citycode": "1", "countrycode": "57"},
                    {"number": "7654321", "citycode": "2", "contrycode": "58"}
                  ]
                }""".formatted(email, password);
    }

    private static String bodyOf(MvcResult result) throws Exception {
        return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Registra el usuario y lo persiste con token, clave con hash y teléfonos")
    void registersAndPersistsUser() throws Exception {
        String email = uniqueEmail();

        MvcResult result = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWith(email, "hunter2")))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isactive").value(true))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();

        String json = bodyOf(result);
        String id = JsonPath.read(json, "$.id");
        String token = JsonPath.read(json, "$.token");
        String created = JsonPath.read(json, "$.created");
        String lastLogin = JsonPath.read(json, "$.last_login");

        assertDoesNotThrow(() -> UUID.fromString(id));
        assertTrue(token.matches(JWT_FORMAT), "El token debe ser un JWT de tres partes");
        assertEquals(created, lastLogin, "En un usuario nuevo last_login coincide con created");

        // Lo que quedó en la base de datos
        User saved = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        assertEquals(id, saved.getIdUser());
        assertEquals(token, saved.getToken());
        assertNotEquals("hunter2", saved.getPassword());
        assertTrue(passwordEncoder.matches("hunter2", saved.getPassword()),
                "La clave debe estar guardada con hash BCrypt");

        Set<String> numbers = saved.getPhones().stream().map(Phone::getNumber).collect(Collectors.toSet());
        Set<String> countryCodes = saved.getPhones().stream().map(Phone::getCountryCode).collect(Collectors.toSet());
        assertEquals(Set.of("1234567", "7654321"), numbers);
        assertEquals(Set.of("57", "58"), countryCodes); // el 58 llegó como "contrycode"
    }

    @Test
    @DisplayName("Rechaza un correo repetido sin distinguir mayúsculas: 409 y un solo usuario")
    void rejectsDuplicateEmailIgnoringCase() throws Exception {
        String email = uniqueEmail();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(bodyWith(email, "hunter2")))
                .andExpect(status().isCreated());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWith(email.toUpperCase(Locale.ROOT), "hunter2")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(Constants.EXIST_MAIL));

        long users = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .count();
        assertEquals(1, users);
    }

    @Test
    @DisplayName("Rechaza una contraseña que no cumple la regex configurada")
    void rejectsPasswordOutsideConfiguredRegex() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWith(uniqueEmail(), "abc12")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(Constants.REGEX_PASSWORD));
    }

    @Test
    @DisplayName("Con varios campos inválidos, el mensaje reúne todos los errores")
    void reportsAllValidationErrors() throws Exception {
        String body = """
                {"name": "", "email": "mal", "password": "", "phones": []}""";

        MvcResult result = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String mensaje = JsonPath.read(bodyOf(result), "$.mensaje");
        for (String esperado : List.of(Constants.BLANK_NAME, Constants.REGEX_MAIL,
                Constants.BLANK_PASSWORD, Constants.EMPTY_PHONES)) {
            assertTrue(mensaje.contains(esperado), "Falta el mensaje: " + esperado);
        }
    }

    @Test
    @DisplayName("Una ruta inexistente responde 404 en formato {mensaje}")
    void unknownRouteReturnsJson404() throws Exception {
        mockMvc.perform(get("/ruta-que-no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value(Constants.NOT_FOUND));
    }

    @Test
    @DisplayName("La especificación OpenAPI está publicada y describe el endpoint de registro")
    void openApiDocumentIsPublished() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("API de registro de usuarios"))
                .andExpect(jsonPath("$.paths['/register/create'].post").exists());
    }

}
