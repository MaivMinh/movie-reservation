package com.foolish.moviereservation.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class ResourceAlreadyException extends AbstractException{
  public ResourceAlreadyException(String message, Map<String, String> details) {
    super(message, details);
  }
}

