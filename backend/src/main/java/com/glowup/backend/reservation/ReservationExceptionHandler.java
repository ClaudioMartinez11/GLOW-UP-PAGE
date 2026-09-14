package com.glowup.backend.reservation;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = exception.getBindingResult().getFieldErrors().stream()
            .collect(java.util.stream.Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage() == null ? "Valor invalido" : error.getDefaultMessage(),
                (first, ignored) -> first
            ));
        return ResponseEntity.badRequest().body(Map.of("error", "Datos de reserva invalidos", "fields", fields));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> generic(Exception exception) {
        return ResponseEntity.internalServerError().body(Map.of("error", "No se pudo procesar la reserva"));
    }
}