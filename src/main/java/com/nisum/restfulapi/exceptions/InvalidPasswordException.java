package com.nisum.restfulapi.exceptions;

import com.nisum.restfulapi.util.Constants;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super(Constants.REGEX_PASSWORD);
    }

}
