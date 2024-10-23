package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private UserController userController;

  @MockBean
  private UserService userService;

  List<UserDTO> userDTOs;

  @BeforeEach
  void setUp() {
    userDTOs = new ArrayList<>();
    UserDTO dto1 = new UserDTO(2, "minhFoolish", "avatar");
    UserDTO dto2 = new UserDTO(3, "username", "avatar");
    UserDTO dto3 = new UserDTO(4, "maivanminh", "avatar");
    userDTOs.add(dto1);
    userDTOs.add(dto2);
    userDTOs.add(dto3);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testGetUserDetailsSuccess() throws Exception {
    // given.
    BDDMockito.given(userService.findByUserId(2)).willReturn(this.userDTOs.get(0));

    // when.
    // Bởi vì request phải qua JwtValidator nên chúng ta phải thêm cookie là access_token vào để có thể đi qua được Filter này.
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/2").cookie(new Cookie("access_token", "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJNb3ZpZSBSZXNlcnZhdGlvbiBTeXN0ZW0iLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6Im1pbmhGb29saXNoIiwiYXV0aG9yaXRpZXMiOiJVU0VSIiwiaWF0IjoxNzI5NjcyNzQ4LCJleHAiOjE3MzAyNzc1NDh9.IuHqX55E7ikGSP9BlWoIOWkPUIt--cjz7gBiDd_Lg-JPL7-g-nngtWd5_MuGdiJx")).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.message").value("Find one success"))
            .andExpect(jsonPath("$.data.userId").value(2)).andExpect(jsonPath("$.data.username").value("minhFoolish")).andExpect(jsonPath("$.data.avatar").value("avatar"));
    // then.
  }
  @Test
  void testGetUserDetailsNotFound() throws Exception {
    // given.
    BDDMockito.given(userService.findByUserId(10)).willReturn(null);

    // when.
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/2").cookie(new Cookie("access_token", "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJNb3ZpZSBSZXNlcnZhdGlvbiBTeXN0ZW0iLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6Im1pbmhGb29saXNoIiwiYXV0aG9yaXRpZXMiOiJVU0VSIiwiaWF0IjoxNzI5NjcyNzQ4LCJleHAiOjE3MzAyNzc1NDh9.IuHqX55E7ikGSP9BlWoIOWkPUIt--cjz7gBiDd_Lg-JPL7-g-nngtWd5_MuGdiJx")).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false
            ))
            .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value("User not found"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testGetUserDetailsForbidden() throws Exception {
    // given.
    BDDMockito.given(userService.findByUserId(4)).willReturn(this.userDTOs.get(2));

    // when.
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/4").cookie(new Cookie("access_token", "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJNb3ZpZSBSZXNlcnZhdGlvbiBTeXN0ZW0iLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6Im1pbmhGb29saXNoIiwiYXV0aG9yaXRpZXMiOiJVU0VSIiwiaWF0IjoxNzI5NjcyNzQ4LCJleHAiOjE3MzAyNzc1NDh9.IuHqX55E7ikGSP9BlWoIOWkPUIt--cjz7gBiDd_Lg-JPL7-g-nngtWd5_MuGdiJx")).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false
            ))
            .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value("Forbidden"))
            .andExpect(jsonPath("$.data").isEmpty());
  }


}