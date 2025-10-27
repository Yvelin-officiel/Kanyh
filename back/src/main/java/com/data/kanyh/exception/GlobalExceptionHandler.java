package com.data.kanyh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        Class<?> dtoClass = Objects.requireNonNull(ex.getBindingResult().getTarget()).getClass();

        java.util.List<String> fieldOrder = java.util.Arrays.stream(dtoClass.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .toList();

        String message = ex.getBindingResult().getFieldErrors().stream()
                .sorted(java.util.Comparator.comparingInt(e -> fieldOrder.indexOf(e.getField())))
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining("\n"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}