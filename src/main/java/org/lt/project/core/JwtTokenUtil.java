package org.lt.project.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.lt.project.core.result.ErrorResult;
import org.lt.project.core.result.Result;
import org.lt.project.core.result.SuccessResult;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtil {
    private static final String SECRET_KEY = "lt-secret-key-secret-key-secret-key";
    private static final long EXPIRATION_TIME= 1000 * 60 * 60;//1 saat
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); 
    
    public static String generateToken(String username){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); 
        return Jwts.builder()
        .subject(username)
        .expiration(expirationDate)
        .signWith(key)
        .compact();
    } 
public static Result validateToken(String token){
    
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);
            return new SuccessResult();
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
            return new ErrorResult( e.getMessage());
        }
    }
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
            return claims.getSubject();
    }

}
