package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.constants.ApplicationConstants;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLOutput;
import java.util.Collections;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class OAuth2Service {
  private final GoogleIdTokenVerifier verifier;
  private final Environment env;
  private final UserService userService;
  private final UserMapper userMapper;
  private final RoleService roleService;

  public OAuth2Service(Environment env, UserService userService, UserService userService1, UserMapper userMapper, RoleService roleService) {
    this.env = env;
    this.userService = userService1;
    String client_id = env.getProperty("google.clientId");
    NetHttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(Collections.singletonList(client_id))
            .build();
    this.userMapper = userMapper;
    this.roleService = roleService;
  }

  // (Receive idTokenString by HTTPS POST)
  public UserDTO verifyGoogleIDToken(String idTokenString) {
    GoogleIdToken idToken = null;
    try {
      idToken = verifier.verify(idTokenString);
    } catch (GeneralSecurityException | IOException e) {
      log.error("{} - {}", e.getClass(), e.getMessage());
    }

    if (idToken != null) {
      GoogleIdToken.Payload payload = idToken.getPayload(); // JWT Payload.

      // Get content in payload.
      String username = ApplicationConstants.GOOGLE_PREFIX + payload.getSubject();// Subject lúc này kết hợp với prefix có vai trò là username.
      UserDTO dto = null;
      dto = userService.findUserDTOByUsername(username);
      // Nếu không thấy Resource này thì tạo User mới.
      if (dto == null) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        // Create a new user.
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setPassword("GOOGLE");
        user.setAvatar(pictureUrl);
        user.setRole(roleService.findByRoleId(Role.USER));
        user = userService.save(user);
        return userMapper.toDTO(user);
      }
      // Tồn tại User trong hệ thống. -> Trả về UserDTO tìm thấy.
      return dto;
    }
    throw new BadCredentialsException("Invalid ID Token");
  }

}
