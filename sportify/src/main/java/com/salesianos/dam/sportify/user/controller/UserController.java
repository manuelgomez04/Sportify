package com.salesianos.dam.sportify.user.controller;

import com.salesianos.dam.sportify.security.jwt.access.JwtService;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshToken;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenRequest;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenService;
import com.salesianos.dam.sportify.user.dto.*;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/user/auth/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @PostMapping("/writer/auth/register")
    public ResponseEntity<UserResponse> registerWriter(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createWriter(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @PostMapping("/admin/auth/register")
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createAdmin(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        // Generar el token de refresco
        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));

    }

    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest req) {
        String token = req.refreshToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }

    @PostMapping("/activate/account/")
    public ResponseEntity<?> activateAccount(@RequestBody ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(userService.activateAccount(token)));
    }


    @PutMapping("/edit/me")
    public GetUsuarioDto updateMe(@AuthenticationPrincipal User user, @RequestBody @Valid EditUserDto createUserRequest) {

        User updatedUser = userService.editMe(user, createUserRequest);
        return GetUsuarioDto.of(updatedUser);
    }

    @PutMapping("/edit/{username}")
    public GetUsuarioDto updateUser(@PathVariable String username, @RequestBody @Valid EditUserDto createUserRequest) {

        User updatedUser = userService.editUser(username, createUserRequest);
        return GetUsuarioDto.of(updatedUser);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User user) {
        return UserResponse.of(user);
    }

    @GetMapping("/me/admin")
    public User adminMe(@AuthenticationPrincipal User user) {
        return user;
    }


    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<?> deleteMe(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
