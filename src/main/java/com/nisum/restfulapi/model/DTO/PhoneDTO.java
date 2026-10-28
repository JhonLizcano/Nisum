package com.nisum.restfulapi.model.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.nisum.restfulapi.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Teléfono del usuario")
public class PhoneDTO {

    @Schema(description = "Número de teléfono (solo dígitos, máximo 10)", example = "1234567")
    @NotNull(message = Constants.INVALID_PHONE_NUMBER)
    @Pattern(regexp = "\\d{1,10}", message = Constants.INVALID_PHONE_NUMBER)
    private String number;

    @Schema(description = "Código de ciudad (solo dígitos, máximo 4)", example = "1")
    @NotNull(message = Constants.INVALID_CITY_CODE)
    @Pattern(regexp = "\\d{1,4}", message = Constants.INVALID_CITY_CODE)
    private String citycode;

    @Schema(description = "Código de país (solo dígitos, máximo 4)", example = "57")
    @NotNull(message = Constants.INVALID_COUNTRY_CODE)
    @Pattern(regexp = "\\d{1,4}", message = Constants.INVALID_COUNTRY_CODE)
    private String countrycode;

}
