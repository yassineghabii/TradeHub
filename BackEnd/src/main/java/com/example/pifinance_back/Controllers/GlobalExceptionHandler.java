package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.AucunOrdreVenteLimiteException;
import com.example.pifinance_back.Entities.TitreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TitreNotFoundException.class)
    public ResponseEntity<ReponseOrdre> handleTitreNotFound(TitreNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AucunOrdreVenteLimiteException.class, RuntimeException.class})
    public ResponseEntity<ReponseOrdre> handleBadRequestException(RuntimeException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ReponseOrdre> handleGeneralException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ReponseOrdre> buildErrorResponse(Exception ex, HttpStatus status) {
        String errorMessage = "Erreur : " + ex.getMessage();
        return ResponseEntity.status(status).body(new ReponseOrdre(errorMessage));
    }
}
