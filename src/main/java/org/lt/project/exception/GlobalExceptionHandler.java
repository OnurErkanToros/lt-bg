package org.lt.project.exception;

import org.lt.project.dto.ErrorResponseDto;
import org.lt.project.exception.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
        // Genel hataları loglayın ama kullanıcıya detay vermeyin
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDto("An internal error occurred", 500));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponseDto("Invalid input provided", 400));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponseDto("Authentication failed", 401));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 401), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictException(ConflictException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 409), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 403), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .findFirst()
            .orElse("Validation error");

        return new ResponseEntity<>(
            new ErrorResponseDto(message, 400),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleFileOperationException(FileOperationException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
