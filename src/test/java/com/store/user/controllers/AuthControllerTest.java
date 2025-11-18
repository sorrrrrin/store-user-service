package com.store.user.controllers;

import com.store.user.dtos.LoginRequestDto;
import com.store.user.dtos.LoginResponseDto;
import com.store.user.dtos.UserResponseDto;
import com.store.user.services.UserService;
import com.store.user.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
        authController.authenticationManager = authenticationManager;
        authController.userDetailsService = userDetailsService;
        authController.userService = userService;
        authController.jwtUtil = jwtUtil;
    }

    @Test
    void loginTest() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken("testuser", Collections.emptyList())).thenReturn("jwtToken");
        when(jwtUtil.generateRefreshToken("testuser")).thenReturn("refreshToken");

        UserResponseDto userResponse = UserResponseDto.builder()
                .username("testuser")
                .name("Test User")
                .email("test@example.com")
                .enabled(true)
                .build();

        when(userService.getUserByUsername("testuser")).thenReturn(userResponse);

        ResponseEntity<LoginResponseDto> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getAccessToken());
        assertEquals("refreshToken", response.getBody().getRefreshToken());
        assertNotNull(response.getBody().getUser());
        assertEquals("testuser", response.getBody().getUser().getUsername());
    }

    @Test
    void loginInvalidCredentialsTest() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager)
                .authenticate(any());

        ResponseEntity<LoginResponseDto> response = null;
        try {
            response = authController.login(loginRequest);
        } catch (RuntimeException e) {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void refreshTest() {
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("testuser");

        when(jwtUtil.validateToken(refreshToken)).thenReturn(claims);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken("testuser", Collections.emptyList())).thenReturn("newJwtToken");

        ResponseEntity<Map<String, String>> response = authController.refresh(refreshToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newJwtToken", response.getBody().get("accessToken"));
    }

    @Test
    void refreshInvalidTokenTest() {
        String refreshToken = "invalidRefreshToken";

        when(jwtUtil.validateToken(refreshToken)).thenThrow(new RuntimeException("Invalid token"));

        ResponseEntity<Map<String, String>> response = authController.refresh(refreshToken);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void logoutTest() {
        String token = "Bearer validToken";

        String response = authController.logout(token);

        assertEquals("", response);
        assertTrue(AuthController.isTokenInvalidated("validToken"));
    }
}