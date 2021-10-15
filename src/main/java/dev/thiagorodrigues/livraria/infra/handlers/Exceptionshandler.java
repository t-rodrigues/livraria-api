package dev.thiagorodrigues.livraria.infra.handlers;

import dev.thiagorodrigues.livraria.application.exceptions.StandardError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class Exceptionshandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        var error = buildError(HttpStatus.BAD_REQUEST.value(), ex.getClass().getSimpleName(), "Validation fails",
                request.getRequestURI());

        ex.getFieldErrors().forEach(f -> error.addValidationError(f.getField(), f.getDefaultMessage()));

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName(),
                "Unknown error occurred", request.getRequestURI());
    }

    private ResponseEntity<StandardError> buildErrorResponse(HttpStatus status, String error, String message,
            String path) {
        var errorResponse = buildError(status.value(), error, message, path);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private StandardError buildError(Integer status, String error, String message, String path) {
        return new StandardError.Builder().status(status).error(error).message(message).path(path).build();
    }

}
