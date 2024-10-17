package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Token;
import com.foolish.moviereservation.repository.TokenRepo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @InjectMocks
  private TokenService tokenService;

  @Mock
  private TokenRepo tokenRepo;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testSaveSuccessWhenValidToken() {
    // Given.
    Token token = new Token(
            1,
            "123456789",
            "minhFoolish",
            new Timestamp(new Date().getTime() + 1048000)
    );
    given(tokenRepo.save(token)).willReturn(token);

    // When.
    Token receivedToken = tokenService.save(token);

    // Then.
    assertNotNull(receivedToken);
    assertEquals(token.getTokenId(), receivedToken.getTokenId());
    assertEquals(token.getUsername(), receivedToken.getUsername());
    assertEquals(token.getToken(), receivedToken.getToken());
    assertEquals(token.getValidUntil(), receivedToken.getValidUntil());
    verify(tokenRepo, times(1)).save(token);
  }
  @Test
  void testSaveReturnNullWhenInputIsNull() {
    Token receivedToken = tokenService.save(null);
    assertNull(receivedToken);
    verify(tokenRepo, times(0)).save(null);
  }
  @Test
  void testSaveFailureWhenUsernameIsNull() {
    Token token = new Token(
            1, "123456789", null, new Timestamp(new Date().getTime())
    );

    // When.
    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    // Then.
    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Username cannot be empty or null");
    verify(tokenRepo, times(0)).save(token);
  }
  @Test
  void testSaveFailureWhenUsernameIsEmpty() {
    Token token = new Token(
            1, "123456789", "", new Timestamp(new Date().getTime())
    );

    // When.
    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    // Then.
    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Username cannot be empty or null");
    verify(tokenRepo, times(0)).save(token);
  }
  @Test
  void testSaveFailureWhenTokenIsNull() {
    Token token = new Token(
            1, null,"minhFoolish", new Timestamp(new Date().getTime())
    );

    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Token cannot be empty or null");
    verify(tokenRepo, times(0)).save(token);
  }
  @Test
  void testSaveFailureWhenTokenIsEmpty() {
    Token token = new Token(
            1, "","minhFoolish", new Timestamp(new Date().getTime())
    );

    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Token cannot be empty or null");
    verify(tokenRepo, times(0)).save(token);
  }
  @Test
  void testSaveFailureWhenValidUntilIsNull() {
    Token token = new Token(
            1, "123456789", "minhFoolish", null
    );

    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Valid until cannot be null or in the past");
    verify(tokenRepo, times(0)).save(token);
  }
  @Test
  void testSaveFailureWhenValidUntilIsInThePast() {
    Token token = new Token(
            1, "123456789", "minhFoolish", new Timestamp(new Date().getTime() - 10000)  // 10s trước hiện tại.
    );

    Throwable thrown = catchThrowable(() -> {
      Token receivedToken = tokenService.save(token);
    });

    Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Valid until cannot be null or in the past");
    verify(tokenRepo, times(0)).save(token);
  }


  @Test
  void testFindByTokenSuccess() {
    // Given.
    Token token = new Token(
            1, "123456789", "minhFoolish", new Timestamp(new Date().getTime())
    );
    String encode = "123456789";

    given(tokenRepo.findByToken(encode)).willReturn(token);

    // When.
    Token foundToken = tokenService.findByToken(encode);

    // Then.
    assertNotNull(foundToken);
    assertEquals(encode, foundToken.getToken());
    verify(tokenRepo, times(1)).findByToken(encode);
  }
  @Test
  void testFindByTokenWhenInputIsNull() {
    String token = null;

    Throwable thrown = catchThrowable(() -> {
      Token foundToken = tokenService.findByToken(token);
    });

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Token cannot be empty or null");
    verify(tokenRepo, times(0)).findByToken(token);
  }
  @Test
  void testFindByTokenWhenInputIsEmpty() {
    String token = "";

    Throwable thrown = catchThrowable(() -> {
      Token foundToken = tokenService.findByToken(token);
    });

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Token cannot be empty or null");
    verify(tokenRepo, times(0)).findByToken(token);
  }

  @Test
  void testDeleteByTokenSuccess() {
    String token = "123456789";
    given(tokenRepo.deleteByToken(token)).willReturn(new Token(1, "123456789", "minhFoolish", new Timestamp(new Date().getTime())));
    Token deletedToken = tokenService.deleteByToken(token);
    assertNotNull(deletedToken);
    assertEquals(token, deletedToken.getToken());
    verify(tokenRepo, times(1)).deleteByToken(token);
  }

  @Test
  void testDeleteByTokenFailureWhenInputIsNull() {
    Throwable thrown = catchThrowable(() -> {
      Token deletedToken = tokenService.deleteByToken(null);
    });

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("Token cannot be null");
    verify(tokenRepo, times(0)).deleteByToken(null);
  }
}