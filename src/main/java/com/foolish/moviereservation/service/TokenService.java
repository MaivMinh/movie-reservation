package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Token;
import com.foolish.moviereservation.repository.TokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
  private final TokenRepo repo;

  public Token save(Token token) {
    return repo.save(token);
  }

  public Token findByToken(String token) {
    return repo.findByToken(token);
  }

  public Token deleteByToken(String token) {
    return repo.deleteByToken(token);
  }
}
