package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.IdTokenDTO;
import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.exceptions.ResourceAlreadyExistedException;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.Token;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.records.LoginRequest;
import com.foolish.moviereservation.records.LoginResponse;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.response.ResponseError;
import com.foolish.moviereservation.service.*;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.foolish.moviereservation.exceptions.ExceptionMessage.RESOURCE_ALREADY_EXISTS;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final RoleService roleService;
  private final AuthenticationManager authenticationManager;
  private final Environment env;
  private final TokenService tokenService;
  private final OAuth2Service oAuth2Service;

  @PostMapping("/register")
  public ResponseEntity<ResponseData> signUp(@RequestBody @Valid User user) {
    // Hàm tạo một User mới bên trong hệ thống.
    UserDTO result = null;
    result = userService.findUserDTOByUsername(user.getUsername());
    if (result != null)
      return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseError(HttpStatus.CONFLICT.value(), "User already exists"));
    // Không tìm thấy Username. Tạo User mới.
    user.setRole(roleService.findByRoleId(Role.USER));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    try {
      user = userService.save(user);
    } catch (RuntimeException e) {
      log.error("Failed to create user: {}", user.getUsername(), e);
      return ResponseEntity.ok().body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to register a new user"));
    }

    if (user.getId() > 0) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.CREATED.value(), "Created successfully", userDTO));
    }
    return ResponseEntity.ok().body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to register a new user"));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseData> login(@RequestBody LoginRequest loginRequest) {
    String jwt = "";
    String refreshToken = "";
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
    Authentication authenticationResponse = authenticationManager.authenticate(authentication); // Thực hiện authenticate bằng cách dùng @Bean Manager đã tạo trong ProjectConfigSecurity để xác thực.

    UserDTO dto = null;
    if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
      if (null != env) {
        // Thực hiện việc tạo access-token và refresh-token.

        String username = authenticationResponse.getName();
        refreshToken = RefreshTokenService.generateRefreshToken(username); // Vì mã hoá thông tin dựa vào username nên chuỗi mã hoá mặc định là duy nhất.

        Token token = tokenService.findByToken(refreshToken);
        if (token == null) {
          token = new Token();
          token.setToken(refreshToken);
          token.setUsername(username);
          token.setValidUntil(new Timestamp(new Date(new Date().getTime() + 30 * 24 * 3600 * 1000L).getTime()));  // Có thời hạn 30 ngày.
          token = tokenService.save(token);
        } else refreshToken = token.getToken();
        String secret = env.getProperty("SECRET_KEY");
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().setIssuer("Movie Reservation System").setSubject("Access Token")
                .claim("username", authenticationResponse.getName())
                .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 7 * 24 * 3600 * 1000L))
                .signWith(secretKey).compact();
      } else log.error("COULD NOT FIND ENVIRONMENT VARIABLE!");
    } else {
      log.error("UNAUTHENTICATED USER!");
      return ResponseEntity.status(200).body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()));
    }

    // Thống nhất là sẽ chỉ gửi access-token và refresh-token ở cookies. access-token sẽ có thời hạn là 7 days, refresh-token là 15 days.
    ResponseCookie refreshCookie = ResponseCookie.from("refresh_token").value(refreshToken).httpOnly(true).path("/api/v1").maxAge(30 * 24 * 3600L).build();

    // Thống nhất không gửi dto.
    return ResponseEntity.ok().header("Set-Cookie", refreshCookie.toString()).body(new ResponseData(HttpStatus.OK.value(), "Login successfully", new LoginResponse("Bearer", jwt, new Timestamp(new Date().getTime() + 7 * 24 * 3600 * 1000L))));
  }

  // Tạo ra API để refresh access token.
  @GetMapping("/refresh-token")
  public ResponseEntity<ResponseData> refreshToken(HttpServletRequest request) {
    StringBuilder refreshToken = new StringBuilder();
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh_token")) {
        // Tìm thấy được refresh_token.
        refreshToken.append(cookie.getValue());
        break;
      }
    }
    StringBuilder accessToken = new StringBuilder();
    String value = request.getHeader("Authorization");
    accessToken.append(value.substring(7)); // Bỏ qua Bearer.

    // Xác thực xem access-token đã hết hạn hay chưa.
    Token token = null;
    String secret = env.getProperty("SECRET_KEY");
    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    boolean isExpired = false;

    try {
      Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken.toString()).getBody();
    } catch (ExpiredJwtException e) {
      // Token thực sự hết hạn.
      log.warn("Access token has truly expired");
      token = tokenService.findByToken(accessToken.toString());
      isExpired = token.getJwt().contentEquals(accessToken);
    } catch (RuntimeException e) {
      throw new RuntimeException("Validate JWT token failed!");
    }
    if (!isExpired) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.FORBIDDEN.value(), "Access token didn't expire!"));
    }
    // Phải xét 2 trường hợp: refresh token còn hạn và refresh token hết hạn.

    // 1. Xét trường hợp refresh token còn hạn.
    Timestamp current = new Timestamp(new Date().getTime());
    if (token.getTokenId() > 0 && token.getValidUntil().getTime() > current.getTime()) {
      // Trả về cho Client một access-token mới.
      UserDTO dto = userService.findUserDTOByUsername(token.getUsername());
      String jwt = Jwts.builder().setIssuer("Backend Advanced Web").setSubject("Access Token")
              .claim("username", dto.getUsername())
              .claim("roles", dto.getRole())
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + 7 * 24 * 3600 * 1000L))
              .signWith(secretKey).compact();
      return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Issued a new access token!", new LoginResponse("Bearer", jwt, new Timestamp(new Date().getTime() + 7 * 24 * 3600 * 1000L))));
    }
    // 2. Xét trường hợp là hết hạn, xoá refresh token dưới DB rồi sau đó trả về response yêu cầu Client đăng nhập lại.
    Token deletedToken = tokenService.deleteByToken(refreshToken.toString());
    // deleted successfully.
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), "refresh token is expired, please login again!"));
  }

  @PostMapping(value = "/verify")
  public ResponseEntity<ResponseData> verifyToken(@RequestBody IdTokenDTO iDToken) {
    try {
      UserDTO dto = oAuth2Service.verifyGoogleIDToken(iDToken.getToken());
      String secret = env.getProperty("SECRET_KEY");
      SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
      // Chỉ tạo ra access-token cho người dùng loại này. Hết hạn thì phải đăng nhập lại hệ thống.
      String jwt = Jwts.builder().setIssuer("Movie Reservation System").setSubject("Access Token")
              .claim("username", dto.getUsername())
              .claim("authorities", dto.getRole().getName())
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + 7 * 24 * 3600 * 1000L))
              .signWith(secretKey).compact();
      return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Login successfully", new LoginResponse("Bearer", jwt, new Timestamp(new Date().getTime() + 3 * 24 * 3600 * 1000L)))); // Cho thời gian của đăng nhập bằng Social ít hơn thông thường.
    } catch (BadCredentialsException e) {
      return ResponseEntity.ok(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
  }
}
