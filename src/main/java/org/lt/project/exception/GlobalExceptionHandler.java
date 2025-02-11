package org.lt.project.exception;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.ErrorResponseDto;
import org.lt.project.exception.customExceptions.*;
import org.lt.project.service.TelegramBotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ValidationException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final TelegramBotService telegramBotService;

    private void sendErrorToTelegram(Exception ex, String customMessage) {
        String errorMessage = "⚠️ *Exception Occurred* ⚠️\n"
                + "*Type:* " + ex.getClass().getSimpleName() + "\n"
                + "*Message:* " + ex.getMessage() + "\n"
                + (customMessage != null ? "*Custom Info:* " + customMessage : "");
        telegramBotService.sendMessage(errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        telegramBotService.sendMessage("Exception occurred: " + ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex) {
        sendErrorToTelegram(ex,"Invalid input provided");
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponseDto("Invalid input provided", 400));
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 401), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictException(ConflictException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 409), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(ForbiddenException e) {
        sendErrorToTelegram(e,null);
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
        sendErrorToTelegram(e,message);
        return new ResponseEntity<>(
            new ErrorResponseDto(message, 400),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleFileOperationException(FileOperationException e) {
        sendErrorToTelegram(e,null);
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
        sendErrorToTelegram(ex,null);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("An internal error occurred", 500));
    }
}
