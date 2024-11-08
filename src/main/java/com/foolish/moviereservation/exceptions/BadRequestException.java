package com.foolish.moviereservation.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
public class BadRequestException extends AbstractException{

  public BadRequestException(String message, Map<String, String> details) {
    super(message, details);
  }
}
