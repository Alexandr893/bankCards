package org.example.bankcards.exception;

import org.example.bankcards.dto.response.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiExceptionResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiExceptionResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        return buildResponse("Пользователь не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiExceptionResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse("Неверные учетные данные", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        var firstError = ex.getBindingResult().getFieldError();
        String message = (firstError != null) ? firstError.getDefaultMessage() : "Ошибка валидации";
        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiExceptionResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ResponseEntity<ApiExceptionResponse> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(
                new ApiExceptionResponse(LocalDateTime.now(), status.value(), message),
                status
        );
    }
}
