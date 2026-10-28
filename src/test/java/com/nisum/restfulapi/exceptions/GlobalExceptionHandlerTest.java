package com.nisum.restfulapi.exceptions;

import com.nisum.restfulapi.controller.RegisterController;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Test Suite")
class GlobalExceptionHandlerTest {

    private static final String URL = "/register/create";
    private static final String VALID_BODY = "{\n" +
            "  \"name\":\"Jhon Parra\", \n" +
            "  \"email\":\"jhon@parra.org\", \n" +
            "  \"password\":\"Hunter\", \n" +
            "  \"phones\": [\n" +
            "    {\n" +
            "      \"number\":\"1234567\", \n" +
            "      \"citycode\":\"1\", \n" +
            "      \"countrycode\":\"57\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @InjectMocks
    RegisterController registerController;

    @Mock
    RegisterService registerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Campo inválido: 400 con el mensaje de validación en JSON")
    void validationError() throws Exception {
        String body = VALID_BODY.replace("\"name\":\"Jhon Parra\"","\"name\":\"\"");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value(Constants.BLANK_NAME));
    }

    @Test
    @DisplayName("Correo con formato inválido: 400")
    void invalidEmailFormat() throws Exception {
        String body = VALID_BODY.replace("jhon@parra.org", "esto-no-es-un-correo");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(Constants.REGEX_MAIL));
    }

    @Test
    @DisplayName("JSON mal formado: 400")
    void malformedJson() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{ 'name': "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(Constants.INVALID_JSON));
    }

    @Test
    @DisplayName("Correo ya registrado: 409 Conflict")
    void emailAlreadyExists() throws Exception {
        when(registerService.saveRegister(any(RequestRegister.class))).thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value(Constants.EXIST_MAIL));
    }

    @Test
    @DisplayName("Contraseña inválida: 400")
    void invalidPassword() throws Exception {
        when(registerService.saveRegister(any(RequestRegister.class)))
                .thenThrow(new InvalidPasswordException());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(Constants.REGEX_PASSWORD));
    }

    @Test
    @DisplayName("Content-Type no JSON: 415")
    void unsupportedMediaType() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.TEXT_PLAIN).content(VALID_BODY))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.mensaje").value(Constants.UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    @DisplayName("Método HTTP no permitido: 405")
    void methodNotAllowed() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.mensaje").value(Constants.METHOD_NOT_ALLOWED));
    }

    @Test
    @DisplayName("Error inesperado: 500 con mensaje genérico, sin filtrar detalles internos")
    void unexpectedError() throws Exception {
        when(registerService.saveRegister(any(RequestRegister.class)))
                .thenThrow(new IllegalStateException("detalle interno que no debe salir"));

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value(Constants.INTERNAL_ERROR));
    }

    @Test
    @DisplayName("Teléfono con letras: 400")
    void invalidPhone() throws Exception {
        String body = """
            {"name": "Jhon Parra", "email": "jhon@parra.org", "password": "Hunter1",
             "phones": [{"number": "12ab", "citycode": "1", "countrycode": "57"}]}""";

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(Constants.INVALID_PHONE_NUMBER));
    }

    @Test
    @DisplayName("Acepta 'contrycode' (el nombre del enunciado) como alias de countrycode")
    void acceptsContrycodeAlias() throws Exception {
        when(registerService.saveRegister(any(RequestRegister.class)))
                .thenReturn(ResponseRegister.builder()
                        .user(UserDto.builder().id("6e8b6014-2c70-478d-9bc1-e50deb22f46b").build())
                        .build());
        String body = """
            {"name": "Jhon Parra", "email": "jhon@parra.org", "password": "Hunter1",
             "phones": [{"number": "1234567", "citycode": "1", "contrycode": "57"}]}""";

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        ArgumentCaptor<RequestRegister> captor = ArgumentCaptor.forClass(RequestRegister.class);
        verify(registerService).saveRegister(captor.capture());
        assertEquals("57", captor.getValue().getPhones().get(0).getCountrycode());
    }

}
