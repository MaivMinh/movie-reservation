package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, Integer> {
  public Token findByToken(String token);
  public Token deleteByToken(String token);
}
