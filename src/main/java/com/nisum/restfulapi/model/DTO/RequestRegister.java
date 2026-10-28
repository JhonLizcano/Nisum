package com.nisum.restfulapi.model.DTO;

import com.nisum.restfulapi.util.Constants;
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
public class RequestRegister {

	@NotBlank(message = Constants.BLANK_NAME)
	private String name;

	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE,
			message = Constants.REGEX_MAIL)
	@NotBlank(message = Constants.BLANK_MAIL)
	private String email;

	@NotBlank(message = Constants.BLANK_PASSWORD)
	private String password;

	@NotEmpty(message = Constants.EMPTY_PHONES)
    private List<PhoneDTO> phones;

}
