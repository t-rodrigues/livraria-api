package dev.thiagorodrigues.livraria.infra.handlers;

import dev.thiagorodrigues.livraria.application.exceptions.ErrorResponse;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        var error = buildError(HttpStatus.BAD_REQUEST.value(), ex.getClass().getSimpleName(), "Validation fails",
                request.getRequestURI());

        ex.getFieldErrors().forEach(f -> error.addValidationError(f.getField(), f.getDefaultMessage()));

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getClass().getSimpleName(), ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getClass().getSimpleName(), ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName(),
                "Unknown error occurred", request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message,
            String path) {
        var errorResponse = buildError(status.value(), error, message, path);

        return ResponseEntity.status(status).body(errorResponse);
    }

    private ErrorResponse buildError(Integer status, String error, String message, String path) {
        return new ErrorResponse.Builder().status(status).error(error).message(message).path(path).build();
    }

}
