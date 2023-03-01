package com.nisum.restfulapi.model.DTO;

import com.nisum.restfulapi.util.Constants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
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
