package org.lt.project.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.lt.project.dto.resultDto.ErrorResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.dto.resultDto.SuccessResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenService {
    private final long EXPIRATION_TIME = 1000 * 60 * 60;//1 saat
    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Result validateToken(String token, UserDetails userDetails) {
        String username = extractUser(token);
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        if (username.equals(userDetails.getUsername())) {
            try {
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parse(token);
                return new SuccessResult();
            } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
                return new ErrorResult(e.getMessage());
            }
        } else {
            return new ErrorResult("Invalid token");
        }

    }

    public String extractUser(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

}
