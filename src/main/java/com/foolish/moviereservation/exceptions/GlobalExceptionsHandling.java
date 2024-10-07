package com.foolish.moviereservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionsHandling {

  @ExceptionHandler({AbstractException.class})
  public ResponseEntity<ExceptionResponse> handleAbstractionExceptions(AbstractException exception) {
    return ResponseEntity.status(exception.getStatus())
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ExceptionResponse(exception.getMessage(), null));
  }
}
