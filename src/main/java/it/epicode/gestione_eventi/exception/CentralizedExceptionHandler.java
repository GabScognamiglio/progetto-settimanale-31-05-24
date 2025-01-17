package it.epicode.gestione_eventi.exception;

import it.epicode.gestione_eventi.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> BadRequestHandler (BadRequestException e) {
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> UnauthorizedHandler (UnauthorizedException e) {
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> NotFoundHandler (NotFoundException e) {
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setDate(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
