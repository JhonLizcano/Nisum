package com.nisum.restfulapi.model.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDTO {

    private long number;
    private int citycode;
    private int countrycode;
}
