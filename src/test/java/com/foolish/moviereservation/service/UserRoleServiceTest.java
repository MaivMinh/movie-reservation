package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.repository.UserRoleRepo;
import jakarta.persistence.SqlResultSetMapping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

  @Mock
  private UserRoleRepo userRoleRepo;

  @InjectMocks
  private UserRoleService userRoleService;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindByUserAndRoleSuccess() {
    // given.
    User user = new User(
            1,
            "username",
            "password",
            "0123456789",
            "example@gmail.com",
            Date.valueOf("2000-10-10"),
            "avatar",
            null
    );
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    user.setUserRoles(List.of(userRole));
    role.setUserRoles(List.of(userRole));

    given(userRoleRepo.findByUserAndRole(user, role)).willReturn(userRole);

    // when.
    UserRole result = userRoleService.findByUserAndRole(user, role);

    // Then.
    assertNotNull(result);
    assertEquals(1, result.getId());
    assertEquals(user, result.getUser());
    assertEquals(role, result.getRole());
    verify(userRoleRepo, times(1)).findByUserAndRole(user, role);
  }
  @Test
  void testFindByUserAndRoleNotFound() {
    User user = new User();
    Role role = new Role(2, "USER", null);
    given(userRoleRepo.findByUserAndRole(user, role)).willReturn(null);

    UserRole userRole = userRoleService.findByUserAndRole(user, role);

    assertNull(userRole);
  }


  @Test
  void testSaveSuccess() {
    User user = new User(
            1,
            "username",
            "password",
            "0123456789",
            "example@gmail.com",
            Date.valueOf("2000-10-10"),
            "avatar",
            null
    );
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    user.setUserRoles(List.of(userRole));
    role.setUserRoles(List.of(userRole));

    given(userRoleRepo.save(userRole)).willReturn(userRole);

    UserRole received =  userRoleService.save(userRole);

    // then.
    assertNotNull(received);
    assertEquals(1, received.getId());
    assertEquals(user, received.getUser());
    assertEquals(role, received.getRole());
    verify(userRoleRepo, times(1)).save(userRole);
  }
  @Test
  void testSaveFailureWhenUserNull() {
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, null, role);
    role.setUserRoles(List.of(userRole));

    Throwable thrown = Assertions.catchThrowable(() -> {
      UserRole received = userRoleService.save(userRole);
    });

    // then.
    assertNotNull(thrown);
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("User and Role are required");
    verify(userRoleRepo, times(0)).save(userRole);
  }
  @Test
  void testSaveFailureWhenRoleNull() {
    User user = new User(
            1,
            "username",
            "password",
            "0123456789",
            "example@gmail.com",
            Date.valueOf("2000-10-10"),
            "avatar",
            null
    );
    UserRole userRole = new UserRole(1, user, null);

    Throwable thrown = Assertions.catchThrowable(() -> {
      UserRole received = userRoleService.save(userRole);
    });

    // then.
    assertNotNull(thrown);
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage("User and Role are required");
    verify(userRoleRepo, times(0)).save(userRole);
  }


  @Test
  void testFindAllByUserSuccess() {
    User user = new User(
            1,
            "username",
            "password",
            "0123456789",
            "example@gmail.com",
            Date.valueOf("2000-10-10"),
            "avatar",
            null
    );
    Role role = new Role(2, "USER", null);
    UserRole userRole = new UserRole(1, user, role);
    List<UserRole> list = List.of(userRole);
    user.setUserRoles(list);
    role.setUserRoles(list);

    given(userRoleRepo.findAllByUser(user)).willReturn(list);

    // when.
    List<UserRole> result = userRoleService.findAllByUser(user);

    // then.
    assertNotNull(result);
    assertEquals(list.size(), result.size());
    assertEquals(list.get(0).getId(), result.get(0).getId());
    assertEquals(list.get(0).getUser(), result.get(0).getUser());
    assertEquals(list.get(0).getRole(), result.get(0).getRole());
    verify(userRoleRepo, times(1)).findAllByUser(user);
  }
  
}