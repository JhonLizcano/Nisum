package com.nisum.restfulapi.util;

import com.nisum.restfulapi.model.User;
import io.jsonwebtoken.Jwts;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTokenUtil {

    public static String generateToken(User user) {
        return Jwts.builder()
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .setSubject(user.getName())
                .setId(user.getIdUser())
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plus(Duration.of(15, ChronoUnit.MINUTES))
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }
}
