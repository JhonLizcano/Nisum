package com.nisum.restfulapi.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del usuario creado")
public class UserDto {

    @Schema(description = "Identificador único (UUID)", example = "6e8b6014-2c70-478d-9bc1-e50deb22f46b")
    private String id;

    @Schema(description = "Fecha de creación del usuario")
    private LocalDateTime created;

    @Schema(description = "Fecha de la última actualización del usuario")
    private LocalDateTime modified;

    @Schema(description = "Fecha del último ingreso (en un usuario nuevo coincide con created)")
    private LocalDateTime last_login;

    @Schema(description = "Token JWT de acceso, firmado con HS256")
    private String token;

    @Schema(description = "Indica si el usuario sigue habilitado dentro del sistema", example = "true")
    private Boolean isactive;

}
