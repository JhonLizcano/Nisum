package com.nisum.restfulapi.model.DTO;

import com.nisum.restfulapi.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar un usuario")
public class RequestRegister {

    @Schema(description = "Nombre completo", example = "Juan Rodriguez")
    @NotBlank(message = Constants.BLANK_NAME)
    private String name;

    @Schema(description = "Correo electrónico (no se distingue entre mayúsculas y minúsculas)",
            example = "juan@rodriguez.org")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = Constants.REGEX_MAIL)
    @NotBlank(message = Constants.BLANK_MAIL)
    private String email;

    @Schema(description = "Contraseña. El formato es configurable en registerapi.constants.passregexp "
            + "(por defecto: 6 a 64 caracteres alfanuméricos)",
            example = "hunter2", format = "password")
    @NotBlank(message = Constants.BLANK_PASSWORD)
    private String password;

    @Schema(description = "Teléfonos del usuario (al menos uno)")
    @NotEmpty(message = Constants.EMPTY_PHONES)
    private List<@Valid PhoneDTO> phones;

}
