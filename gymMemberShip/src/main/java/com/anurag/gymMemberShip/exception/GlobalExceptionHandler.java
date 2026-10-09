package com.anurag.gymMemberShip.exception;

import java.util.Collections;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

      @ExceptionHandler(ResourceNotFoundException.class)
      public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", ex.getMessage()));
      }

      @ExceptionHandler(IllegalArgumentException.class)
      public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", ex.getMessage()));
      }

      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
            FieldError fieldError = ex.getBindingResult().getFieldError();
            String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Validation error";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", errorMessage));
      }

      @ExceptionHandler(BadCredentialsException.class)
      public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid email or password"));
      }

      @ExceptionHandler(DataIntegrityViolationException.class)
      public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
            String msg = "A database constraint was violated. Please verify unique fields like email and phone.";
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("duplicate")) {
                  msg = "An account with this email or phone number already exists.";
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message", msg));
      }

      @ExceptionHandler(Exception.class)
      public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", msg));
      }
}
