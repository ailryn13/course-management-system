package com.example.studentapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppExceptions.ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(AppExceptions.ResourceNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", e.getMessage());
    }

    @ExceptionHandler(AppExceptions.CourseFullException.class)
    public ResponseEntity<Map<String, String>> handleCourseFull(AppExceptions.CourseFullException e) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getMessage());
    }

    @ExceptionHandler(AppExceptions.DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResource(AppExceptions.DuplicateResourceException e) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", e.getMessage());
    }

    @ExceptionHandler(AppExceptions.BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(AppExceptions.BadRequestException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage());
    }

    @ExceptionHandler(AppExceptions.UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(AppExceptions.UnauthorizedException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        e.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred.");
    }

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}