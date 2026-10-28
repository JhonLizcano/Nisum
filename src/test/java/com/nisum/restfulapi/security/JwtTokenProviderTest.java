package com.nisum.restfulapi.security;

import com.nisum.restfulapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("JwtTokenProvider Test Suite")
class JwtTokenProviderTest {

    private static final String SECRET = "secreto-de-pruebas-mas-32-caracteres";
    private static final long EXPIRATION_MINUTES = 15;

    private JwtTokenProvider tokenProvider;
    private User user;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(SECRET, EXPIRATION_MINUTES);
        user = User.builder()
                .idUser("6e8b6014-2c70-478d-9bc1-e50deb22f46b")
                .name("Jhon Parra")
                .email("jhon@parra.org")
                .build();
    }

    private SecretKey keyOf(String secret) {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Test
    @DisplayName("El token está firmado con HS256 y contiene los claims del usuario")
    void generateTokenIsSignedWithExpectedClaims() {
        String token = tokenProvider.generateToken(user);

        Jws<Claims> jws = Jwts.parser().verifyWith(keyOf(SECRET)).build().parseSignedClaims(token);

        assertEquals("HS256", jws.getHeader().getAlgorithm());
        assertEquals(user.getIdUser(), jws.getPayload().getSubject());
        assertEquals("Jhon Parra", jws.getPayload().get("name", String.class));
        assertEquals("jhon@parra.org", jws.getPayload().get("email", String.class));
        assertNotNull(jws.getPayload().getId());
    }

    @Test
    @DisplayName("La expiración es la configurada")
    void generateTokenExpiresAfterConfiguredMinutes() {
        String token = tokenProvider.generateToken(user);

        Claims claims = Jwts.parser().verifyWith(keyOf(SECRET)).build()
                .parseSignedClaims(token).getPayload();

        long diffMillis = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertEquals(EXPIRATION_MINUTES * 60 * 1000, diffMillis);
    }

    @Test
    @DisplayName("Un token no se puede validar con otra clave")
    void tokenIsRejectedWithDifferentKey() {
        String token = tokenProvider.generateToken(user);
        SecretKey otherKey = keyOf("otro-secreto-completamente-diferente-123456");

        assertThrows(JwtException.class,
                () -> Jwts.parser().verifyWith(otherKey).build().parseSignedClaims(token));
    }

    @Test
    @DisplayName("Dos tokens del mismo usuario son distintos (jti único)")
    void tokensAreUnique() {
        assertNotEquals(tokenProvider.generateToken(user), tokenProvider.generateToken(user));
    }

    @Test
    @DisplayName("Un secreto menor a 32 caracteres impide crear el proveedor")
    void shortSecretIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new JwtTokenProvider("corto", EXPIRATION_MINUTES));
    }

}
