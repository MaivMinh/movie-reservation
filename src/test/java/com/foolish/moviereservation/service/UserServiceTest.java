package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks
  UserService userService;

  @Mock
  UserRepo userRepo;
  @Mock
  UserMapper userMapper;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testFindUserByUsernameSuccess() {
    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));

    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));

    given(userRepo.findByUsername("username")).willReturn(user);
    given(userMapper.toDTO(user)).willReturn(
            new UserDTO(2, "username", "avatar")
    );

    // When
    UserDTO dto = userService.findUserByUserName("username");

    // Then
    assertNotNull(dto);
    assertEquals(2, dto.getUserId());
    assertEquals("username", dto.getUsername());
    assertEquals("avatar", dto.getAvatar());

    verify(userRepo, times(1)).findByUsername("username");
  }
  @Test
  public void testFindUserByUsernameNotFound() {
    // Given.
    given(userRepo.findByUsername(Mockito.anyString())).willReturn(null);

    // When.
    UserDTO dto = userService.findUserByUserName("username");

    // Then.
    assertNull(dto);
    verify(userRepo, times(1)).findByUsername("username");
  }


  @Test
  public void testFindUserByEmailSuccess() {
    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));
    given(userRepo.findByEmail("example@gmail.com")).willReturn(user);
    given(userMapper.toDTO(user)).willReturn(new UserDTO(2, "username", "avatar"));

    // when
    UserDTO dto = userService.findUserByEmail("example@gmail.com");

    // then.
    assertNotNull(dto);
    assertEquals(2, dto.getUserId());
    assertEquals("username", dto.getUsername());
    assertEquals("avatar", dto.getAvatar());
    verify(userRepo, times(1)).findByEmail("example@gmail.com");
  }
  @Test
  public void testFindUserByEmailNotFound() {
    given(userRepo.findByEmail(Mockito.anyString())).willReturn(null);
    given(userMapper.toDTO(null)).willReturn(null);
    UserDTO dto = userService.findUserByEmail("example@gmail.com");
    assertNull(dto);
  }


  @Test
  void testFindByUserIdSuccess() {

    // Given: Preparing input and expected result. Define the behaviour of Mock Object.
    /*
     * 2,
     * username,
     * {bcrypt}$2a$12$GLHmXMdGd90UKZXkcZmXi.5nV2OlJi4ggCELOWQbIwzPAbfkDKK5C,
     * 0345730710,
     * example@gmail.com,
     * 2003-01-01,
     * */

    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setAvatar("avatar");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));

    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));

    given(userRepo.findByUserId(2)).willReturn(user);
    given(userMapper.toDTO(user)).willReturn(new UserDTO(
            2,
            "username",
            "avatar"
    ));
    // When: Execute test method.
    UserDTO dto = userService.findByUserId(2);

    // Then: Comparing received result and expected result.
    assertNotNull(dto);
    assertEquals(2, dto.getUserId());
    assertEquals("username", dto.getUsername());
    assertEquals("avatar", dto.getAvatar());

    verify(userRepo, times(1)).findByUserId(2);
  }
  @Test
  public void testFindByUserIdNotFound() {
    // Given.
    given(userRepo.findByUserId(Mockito.anyInt())).willReturn(null);
    given(userMapper.toDTO(null)).willReturn(null);

    // When.
    UserDTO dto = userService.findByUserId(2);

    // Then.
    assertNull(dto);
    verify(userRepo, times(1)).findByUserId(2);
  }


  @Test
  public void testFindUserByPhoneNumberSuccess() {
    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));

    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));

    given(userRepo.findByPhoneNumber("0345730700")).willReturn(user);
    given(userMapper.toDTO(user)).willReturn(new UserDTO(
            2,
            "username",
            "avatar"
    ));

    // When
    UserDTO dto = userService.findUserByPhoneNumber("0345730700");

    // Then
    assertNotNull(dto);
    assertEquals(2, dto.getUserId());
    assertEquals("username", dto.getUsername());
    assertEquals("avatar", dto.getAvatar());

    verify(userRepo, times(1)).findByPhoneNumber("0345730700");
  }
  @Test
  public void testFindUserByPhoneNumberNotFound() {
    given(userRepo.findByPhoneNumber(Mockito.anyString())).willReturn(null);
    given(userMapper.toDTO(null)).willReturn(null);
    UserDTO dto = userService.findUserByPhoneNumber("0345730700");
    assertNull(dto);
    verify(userRepo, times(1)).findByPhoneNumber(Mockito.anyString());
  }


  @Test
  public void testFindByUsernameSuccess() {
    // Given: Preparing input and expected result. Define the behaviour of Mock Object.
    /*
     * 2,
     * username,
     * {bcrypt}$2a$12$GLHmXMdGd90UKZXkcZmXi.5nV2OlJi4ggCELOWQbIwzPAbfkDKK5C,
     * 0345730710,
     * example@gmail.com,
     * 2003-01-01,
     * */

    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setAvatar("avatar");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));

    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));

    given(userRepo.findByUsername("username")).willReturn(user);
    // When: Execute test method.
    User receivedUser = userService.findByUsername("username");

    // Then.
    assertNotNull(receivedUser);
    assertEquals(2, receivedUser.getUserId());
    assertEquals("username", receivedUser.getUsername());
    assertEquals("password", receivedUser.getPassword());
    assertEquals("0345730700", receivedUser.getPhoneNumber());
    assertEquals("example@gmail.com", receivedUser.getEmail());
    assertEquals("avatar", receivedUser.getAvatar());
    verify(userRepo, times(1)).findByUsername("username");
  }
  @Test
  public void testFindByUsernameNotFound() {
    given(userRepo.findByUsername(Mockito.anyString())).willReturn(null);
    User user = userService.findByUsername("username");
    assertNull(user);
    verify(userRepo, times(1)).findByUsername(Mockito.anyString());
  }


  @Test
  public void testSaveSuccess() {
    User user = new User();
    user.setUserId(2);
    user.setUsername("username");
    user.setPassword("password");
    user.setAvatar("avatar");
    user.setPhoneNumber("0345730700");
    user.setEmail("example@gmail.com");
    user.setBirthDate(Date.valueOf("2003-01-01"));
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    role.setUserRoles(List.of(userRole));
    user.setUserRoles(List.of(userRole));
    given(userRepo.save(user)).willReturn(user);

    // when.
    User savedUser = userService.save(user);

    // then.
    assertNotNull(savedUser);
    assertEquals(2, savedUser.getUserId());
    assertEquals("username", savedUser.getUsername());
    assertEquals("password", savedUser.getPassword());
    assertEquals("0345730700", savedUser.getPhoneNumber());
    assertEquals("example@gmail.com", savedUser.getEmail());
    assertEquals("avatar", savedUser.getAvatar());

    verify(userRepo, times(1)).save(user);
  }
}