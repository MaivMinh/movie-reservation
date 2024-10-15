package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.constants.ApplicationConstants;
import com.foolish.moviereservation.exceptions.ResourceAlreadyExistedException;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.Token;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.records.JwtResponse;
import com.foolish.moviereservation.records.LoginRequest;
import com.foolish.moviereservation.records.LoginResponse;
import com.foolish.moviereservation.service.RefreshTokenService;
import com.foolish.moviereservation.service.RoleService;
import com.foolish.moviereservation.service.TokenService;
import com.foolish.moviereservation.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
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

  @PostMapping("/signup")
  public ResponseEntity<UserDTO> signUp(@RequestBody @Valid User user) {
    // Hàm tạo một User mới bên trong hệ thống.
    UserDTO result = null;
    result = userService.findUserByUserName(user.getUsername());
    if (result != null)
      throw new ResourceAlreadyExistedException(HttpStatus.BAD_REQUEST, RESOURCE_ALREADY_EXISTS.getDescription(), Map.of("username", result.getUsername()));

    result = userService.findUserEmail(user.getEmail());
    if (result != null)
      throw new ResourceAlreadyExistedException(HttpStatus.BAD_REQUEST, RESOURCE_ALREADY_EXISTS.getDescription(), Map.of("email", result.getEmail()));

    result = userService.findUserByPhoneNumber(user.getPhoneNumber());
    if (result != null)
      throw new ResourceAlreadyExistedException(HttpStatus.BAD_REQUEST, RESOURCE_ALREADY_EXISTS.getDescription(), Map.of("phone-number", result.getPhoneNumber()));

    // Set Role cho User. [{1: ADMIN}, {2, USER}]
    Role role = roleService.findByRoleId(Role.USER);
    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(role);
    user.setUserRoles(Collections.singletonList(userRole));

    // Hashing user's password.
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    try {
      user = userService.save(user);
    } catch (RuntimeException e) {
      log.error("Failed to create user: {}", user.getUsername(), e);
      return ResponseEntity.internalServerError().body(null);
    }

    if (user.getUserId() > 0) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.ok(userDTO);
    }
    return ResponseEntity.internalServerError().body(null);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    String jwt = "";
    String refreshToken = "";
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),  // Mục đích là tạo ra một token chưa authenticated để cho Provider có thể authenticate sau này.
            loginRequest.password());
    Authentication authenticationResponse = authenticationManager.authenticate(authentication); // Thực hiện authenticate bằng cách dùng @Bean Manager đã tạo trong ProjectConfigSecurity để xác thực.
    // Lưu ý: Chúng ta không thể sử dụng standard flow bởi vì nếu dùng thì /login endpoint này sẽ phải dùng các Provider được định nghĩa sẵn của Manager trước đó và không thể tạo ra JWT Token được. Còn nếu suy nghĩ đến việc điều chỉnh hàm authenticate của UsernamePwdAuthenticationProvider cũng là không thể bởi vì hàm này chỉ chấp nhận một tham số là Authenticate và cũng chỉ trả về một object là Authenticate.

    if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
      if (null != env) {
        // Thực hiện việc tạo access-token và refresh-token.

        String username = authenticationResponse.getName().toString();
        refreshToken = RefreshTokenService.generateRefreshToken(username); // Vì mã hoá thông tin dựa vào username nên chuỗi mã hoá mặc định là duy nhất.

        Token token = tokenService.findByToken(refreshToken);
        if (token == null) {
          token = new Token();
          token.setToken(refreshToken);
          token.setUsername(username);
          token.setValidUntil(new Timestamp(new Date(new Date().getTime() + 1296000000).getTime()));  // Có thời hạn 15 ngày.
          token = tokenService.save(token);
        } else refreshToken = token.getToken();
        String secret = env.getProperty("SECRET_KEY");
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().setIssuer("Movie Reservation System").setSubject("JWT Token")
                .claim("username", authenticationResponse.getName())
                .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 604800000))
                .signWith(secretKey).compact();
      } else log.error("COULD NOT FIND ENVIRONMENT VARIABLE!");
    } else {
      log.error("USER ISN'T AUTHENTICATED!");
      return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Invalid user's credentials!"));
    }

    // Thống nhất là sẽ chỉ gửi access-token và refresh-token ở cookies. access-token sẽ có thời hạn là 7 days, refresh-token là 15 days.
    ResponseCookie accessCookie = ResponseCookie.from("access_token").value(jwt).httpOnly(true).path("/").maxAge(604800).build();
    ResponseCookie refreshCookie = ResponseCookie.from("refresh_token").value(refreshToken).httpOnly(true).path("/").maxAge(1296000).build();

    return ResponseEntity.ok().header("Set-Cookie", accessCookie.toString()).header("Set-Cookie", refreshCookie.toString()).body(new LoginResponse(HttpStatus.OK.getReasonPhrase(), "Login successfully!"));
  }


  // Tạo ra API để refresh access token.
  @GetMapping("/refreshToken")
  public ResponseEntity<JwtResponse> refreshToken(HttpServletResponse response, Authentication authentication, @RequestBody String refreshToken) {
    Token token = tokenService.findByToken(refreshToken);

    // Phải xét 2 trường hợp: token còn hạn và token hết hạn.

    // 1. Xét trường hợp token còn hạn.
    Timestamp current = new Timestamp(new Date().getTime());
    if (token.getValidUntil().getTime() > current.getTime()) {
      // Trả về cho Client một access-token mới.
      StringBuilder jwt = new StringBuilder();
      
    }

    return null;
  }

}
