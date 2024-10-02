package com.foolish.moviereservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionsHandling {

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(BadRequestException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ExceptionResponse> handleResourceNotFoundExceptions(ResourceNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({ResourceAlreadyException.class})
  public ResponseEntity<ExceptionResponse> handleResourceAlreadyExceptions(ResourceAlreadyException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ExceptionResponse(exception.getMessage(), exception.getDetails()));
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ExceptionResponse> handleRuntimException(RuntimeException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ExceptionResponse(exception.getMessage(), null));
  }
}
