package com.nisum.restfulapi.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.PatternSyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PasswordPolicy Test Suite")
class PasswordPolicyTest {

    private final PasswordPolicy defaultPolicy = new PasswordPolicy("[A-Za-z0-9]{6,64}");

    @ParameterizedTest
    @ValueSource(strings = {"hunter2", "Hunter", "A1b2C3d4"})
    @DisplayName("Claves válidas con la regex por defecto")
    void validPasswords(String password) {
        assertTrue(defaultPolicy.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"short", "hunter2$%", "con espacios1", ""})
    @DisplayName("Claves inválidas con la regex por defecto")
    void invalidPasswords(String password) {
        assertFalse(defaultPolicy.isValid(password));
    }

    @Test
    @DisplayName("Una clave nula es inválida")
    void nullPassword() {
        assertFalse(defaultPolicy.isValid(null));
    }

    @Test
    @DisplayName("La regex es configurable: otra regex cambia el resultado")
    void regexIsConfigurable() {
        PasswordPolicy strict = new PasswordPolicy("^(?=.*[A-Z])(?=.*\\d.*\\d)[A-Za-z0-9]{8,12}$");

        assertTrue(strict.isValid("Abcdef12"));
        assertFalse(strict.isValid("abcdef12"));  // sin mayúscula
        assertFalse(strict.isValid("Abcdefg1"));  // un solo dígito
    }

    @Test
    @DisplayName("Una regex inválida impide crear la política (falla al arrancar)")
    void invalidRegexFailsFast() {
        assertThrows(PatternSyntaxException.class, () -> new PasswordPolicy("[abc"));
    }

}
