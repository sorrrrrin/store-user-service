package com.store.user.controllers;

import com.store.user.dtos.LoginRequestDto;
import com.store.user.dtos.LoginResponseDto;
import com.store.user.dtos.UserResponseDto;
import com.store.user.services.UserService;
import com.store.user.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    private static final Set<String> invalidatedTokens = new HashSet<>();

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        // fetch user data (mapped to a safe DTO that excludes sensitive fields)
        UserResponseDto userDto = userService.getUserByUsername(userDetails.getUsername());

        LoginResponseDto response = LoginResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestParam String refreshToken) {
        try {
            Claims claims = jwtUtil.validateToken(refreshToken);
            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String newJwtToken = jwtUtil.generateToken(username, roles);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newJwtToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        invalidatedTokens.add(token);
        return "";
    }

    public static boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

}
