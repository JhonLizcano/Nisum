package com.nisum.restfulapi.exceptions;

import com.nisum.restfulapi.util.Constants;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super(Constants.EXIST_MAIL);
    }

}
