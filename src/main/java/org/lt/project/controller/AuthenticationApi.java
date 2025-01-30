package org.lt.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lt.project.dto.RegisterRequest;
import org.lt.project.dto.UserLoginRequestDto;
import org.lt.project.dto.UserRegisterResponseDto;
import org.lt.project.security.filter.RateLimitFilter;
import org.lt.project.service.AuthenticationService;
import org.lt.project.service.TelegramBotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication/")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final RateLimitFilter rateLimitFilter;
  private final TelegramBotService telegramBotService;

  private void sendErrorToTelegram(Exception ex, String customMessage) {
    String errorMessage =
        "⚠️ *Exception Occurred* ⚠️\n"
            + "*Type:* "
            + ex.getClass().getSimpleName()
            + "\n"
            + "*Message:* "
            + ex.getMessage()
            + "\n"
            + (customMessage != null ? "*Custom Info:* " + customMessage : "");
    telegramBotService.sendMessage(errorMessage);
    }



    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request) {
    String clientId = getClientId(request);
        if (rateLimitFilter.isBlacklisted(clientId)) {
      sendErrorToTelegram(
          new RuntimeException("Rate limit exceeded"),
          "ipAddress: " + clientId + "\n username: " + requestDto.getUsername());
      return getRateLimitExceededResponse();
        }

        try {
            return ResponseEntity.ok(authenticationService.authenticateUser(requestDto));
        } catch (AuthenticationException e) {
      sendErrorToTelegram(e, "ipAddress: " + clientId + "\n username: " + requestDto.getUsername());
            log.warn("Failed login attempt for user: {}", requestDto.getUsername());
            rateLimitFilter.recordFailedAttempt(clientId);

            if (rateLimitFilter.isBlacklisted(clientId)) {
        sendErrorToTelegram(
            new RuntimeException("Rate limit exceeded"),
            "ipAddress: " + clientId + "\n username: " + requestDto.getUsername());

        return getRateLimitExceededResponse();
      }

      return getUnauthorizedResponse();
    }
  }

  @PostMapping("/register")
  public ResponseEntity<UserRegisterResponseDto> register(
      @Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authenticationService.registerUser(request));
  }

  // Rate limit exceeded response
  private ResponseEntity<ErrorResponse> getRateLimitExceededResponse() {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(
            new ErrorResponse(
                "IP adresiniz geçici olarak engellenmiştir. Lütfen daha sonra tekrar deneyiniz.",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                LocalDateTime.now()));
  }

  // Unauthorized response
  private ResponseEntity<ErrorResponse> getUnauthorizedResponse() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(
            new ErrorResponse(
                "Kullanıcı adı ya da şifre yanlış.",
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()));
  }

  // Get client IP address from request
  private String getClientId(HttpServletRequest request) {
    String clientId = request.getHeader("X-Forwarded-For");
    if (clientId == null) {
      clientId = request.getRemoteAddr();
    }
    return clientId;
    }

  @Data
  @AllArgsConstructor
  public static class ErrorResponse {
        private String message;
        private int statusCode;
        private LocalDateTime timeStamp;
    }
}

