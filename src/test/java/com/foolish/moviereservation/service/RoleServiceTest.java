package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.repository.RoleRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @InjectMocks
  private RoleService roleService;

  @Mock
  RoleRepo roleRepo;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void findByRoleIdSuccess() {
    // given.
    Role expected = new Role(
            2,
            "USER",
            null
    );
    given(roleRepo.findByRoleId(2)).willReturn(expected);

    // when
    Role received = roleService.findByRoleId(2);

    // then.
    assertNotNull(received);
    assertEquals(expected.getRoleId(), received.getRoleId());
    assertEquals(expected.getName(), received.getName());
    verify(roleRepo, times(1)).findByRoleId(2);
  }
  @Test
  void findByRoleIdNotFound() {
    // given.
    given(roleRepo.findByRoleId(2)).willReturn(null);

    // when.
    Role received = roleService.findByRoleId(2);

    // then.
    assertNull(received);
    verify(roleRepo, times(1)).findByRoleId(2);
  }
}