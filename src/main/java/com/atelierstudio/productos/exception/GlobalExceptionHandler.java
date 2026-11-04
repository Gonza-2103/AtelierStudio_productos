package com.atelierstudio.productos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //1. Manejo de errores de validación de Jakarta
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("STATUS", HttpStatus.BAD_REQUEST.value());
        response.put("ERROR", "Error de validación");
        response.put("ERRORES", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //2. Manejo de recurso no encontrado personalizado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("STATUS", HttpStatus.NOT_FOUND.value());
        response.put("ERROR", "Recurso no encontrado");
        response.put("MENSAJE", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //3. Manejo de tipos de datos incorrectos en parámetros URL (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("STATUS", HttpStatus.BAD_REQUEST.value());
        response.put("ERROR", "Parámetro inválido");
        response.put("MENSAJE", String.format("El parámetro '%s' debe ser de tipo numérico", ex.getName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //4. Manejo general de excepciones no controladas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGeneralExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("STATUS", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("ERROR", "Error interno del servidor");
        response.put("MENSAJE", "Ocurrió un error inesperado. Intente más tarde.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
