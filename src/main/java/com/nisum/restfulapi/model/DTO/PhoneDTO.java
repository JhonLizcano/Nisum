package com.nisum.restfulapi.model.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.nisum.restfulapi.util.Constants;
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
public class PhoneDTO {

    @NotNull(message = Constants.INVALID_PHONE_NUMBER)
    @Pattern(regexp = "\\d{1,10}", message = Constants.INVALID_PHONE_NUMBER)
    private String number;

    @NotNull(message = Constants.INVALID_CITY_CODE)
    @Pattern(regexp = "\\d{1,4}", message = Constants.INVALID_CITY_CODE)
    private String citycode;

    @JsonAlias("contrycode")
    @NotNull(message = Constants.INVALID_COUNTRY_CODE)
    @Pattern(regexp = "\\d{1,4}", message = Constants.INVALID_COUNTRY_CODE)
    private String countrycode;

}
