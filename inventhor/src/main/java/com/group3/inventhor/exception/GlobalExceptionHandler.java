package com.group3.inventhor.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Group 3
 *
 * The GlobalExceptionHandler class is a centralized exception handler for the Inventhor application.
 * It handles exceptions thrown by controllers and provides appropriate HTTP responses.
 *
 * @ControllerAdvice indicates that this class provides global exception handling for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EntityNotFoundException and returns a 404 Not Found response.
     *
     * @param e the exception that was thrown
     * @return ResponseEntity with a 404 status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles generic exceptions and returns a 500 Internal Server Error response.
     *
     * @param e the exception that was thrown
     * @return ResponseEntity with a 500 status and error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        e.printStackTrace(); // For debugging
        return ResponseEntity
                .internalServerError()
                .body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: " + ex.getMessage());
    }
}
