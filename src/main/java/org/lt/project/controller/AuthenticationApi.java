package org.lt.project.controller;

import java.time.LocalDateTime;

import org.lt.project.dto.RegisterRequest;
import org.lt.project.dto.UserCreateRequestDto;
import org.lt.project.dto.UserLoginRequestDto;
import org.lt.project.dto.UserLoginResponseDto;
import org.lt.project.dto.UserRegisterResponseDto;
import org.lt.project.model.User;
import org.lt.project.security.filter.RateLimitFilter;
import org.lt.project.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authentication/")
@Slf4j
public class AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final RateLimitFilter rateLimitFilter;

    public AuthenticationApi(AuthenticationService authenticationService, RateLimitFilter rateLimitFilter) {
        this.authenticationService = authenticationService;
        this.rateLimitFilter = rateLimitFilter;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request) {
        String clientId = request.getHeader("X-Forwarded-For");
        if (clientId == null) {
            clientId = request.getRemoteAddr();
        }

        if (rateLimitFilter.isBlacklisted(clientId)) {
            return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse(
                    "IP adresiniz geçici olarak engellenmiştir. Lütfen daha sonra tekrar deneyiniz.",
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    LocalDateTime.now()
                ));
        }

        try {
            return ResponseEntity.ok(authenticationService.authenticateUser(requestDto));
        } catch (AuthenticationException e) {
            log.warn("Failed login attempt for user: {}", requestDto.getUsername());
            
            // Failed attempt'i kaydet
            rateLimitFilter.recordFailedAttempt(clientId);
            
            // Tekrar blacklist kontrolü yap
            if (rateLimitFilter.isBlacklisted(clientId)) {
                return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse(
                        "IP adresiniz geçici olarak engellenmiştir. Lütfen daha sonra tekrar deneyiniz.",
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        LocalDateTime.now()
                    ));
            }
            
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                    "Kullanıcı adı yada şifre yanlış.",
                    HttpStatus.UNAUTHORIZED.value(),
                    LocalDateTime.now()
                ));
        }
        
        
    }
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }


    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String message;
        private int statusCode;
        private LocalDateTime timeStamp;
    }
}