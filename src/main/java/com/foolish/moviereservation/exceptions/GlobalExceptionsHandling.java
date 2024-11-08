package com.foolish.moviereservation.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Order(1)
public class GlobalExceptionsHandling {

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ExceptionResponse> handleBadCredentialsExceptions(BadCredentialsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(exception.getMessage(), null));
  }

  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<ExceptionResponse> handleAbstractionExceptions(ResourceNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({AbstractException.class})
  public ResponseEntity<ExceptionResponse> handleAbstractionExceptions(AbstractException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(exception.getMessage(), null));
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(exception.getMessage(), null));
  }
}
