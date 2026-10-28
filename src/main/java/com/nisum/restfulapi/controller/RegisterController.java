package com.nisum.restfulapi.controller;

import com.nisum.restfulapi.model.DTO.ApiError;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(name = "Registro", description = "Creación de usuarios")
public class RegisterController {

    private final RegisterService registerService;

    @Operation(summary = "Crea un usuario",
            description = "Registra un usuario con sus teléfonos y retorna su id, fechas, estado y un token JWT. "
                    + "La contraseña se almacena con hash y el token se persiste con el usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos: validación de campos, contraseña o JSON mal formado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(name = "Nombre vacío",
                                            value = "{\"mensaje\": \"" + Constants.BLANK_NAME + "\"}"),
                                    @ExampleObject(name = "Correo con formato inválido",
                                            value = "{\"mensaje\": \"" + Constants.REGEX_MAIL + "\"}"),
                                    @ExampleObject(name = "Contraseña con formato inválido",
                                            value = "{\"mensaje\": \"" + Constants.REGEX_PASSWORD + "\"}"),
                                    @ExampleObject(name = "JSON mal formado",
                                            value = "{\"mensaje\": \"" + Constants.INVALID_JSON + "\"}")
                            })),
            @ApiResponse(responseCode = "409", description = "El correo ya está registrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{\"mensaje\": \"" + Constants.EXIST_MAIL + "\"}"))),
            @ApiResponse(responseCode = "415", description = "El Content-Type no es application/json",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{\"mensaje\": \"" + Constants.UNSUPPORTED_MEDIA_TYPE + "\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{\"mensaje\": \"" + Constants.INTERNAL_ERROR + "\"}")))
    })
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createRegister(@Valid @RequestBody RequestRegister requestRegister) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerService.saveRegister(requestRegister));
    }

}
