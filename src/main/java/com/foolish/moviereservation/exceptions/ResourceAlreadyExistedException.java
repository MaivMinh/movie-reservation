package com.foolish.moviereservation.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
public class ResourceAlreadyExistedException extends AbstractException{
  public ResourceAlreadyExistedException(String message, Map<String, String> details) {
    super(message, details);
  }
}

