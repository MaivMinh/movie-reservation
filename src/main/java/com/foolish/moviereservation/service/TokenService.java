package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Token;
import com.foolish.moviereservation.repository.TokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class TokenService {
  private final TokenRepo repo;

  public Token save(Token token) {
    if (token == null)  return null;
    if (token.getUsername() == null || token.getUsername().isEmpty()) throw new IllegalArgumentException("Username cannot be empty or null");

    if (token.getToken() == null || token.getToken().isEmpty()) throw new IllegalArgumentException("Token cannot be empty or null");

    if (token.getValidUntil() == null || token.getValidUntil().getTime() < new Date().getTime()) throw new IllegalArgumentException("Valid until cannot be null or in the past");

    return repo.save(token);
  }

  public Token findByToken(String token) {
    if (token == null || token.isEmpty()) throw new IllegalArgumentException("Token cannot be empty or null");
    return repo.findByToken(token);
  }

  public Token deleteByToken(String token) {
    if (token == null) throw new IllegalArgumentException("Token cannot be null");
    return repo.deleteByToken(token);
  }
}
