package com.nisum.restfulapi.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordPolicy {

    private final Pattern pattern;

    public PasswordPolicy(@Value("${registerapi.constants.passregexp}") String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean isValid(String password) {
        return password != null && pattern.matcher(password).matches();
    }

}
