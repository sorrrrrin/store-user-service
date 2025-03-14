package com.store.user.controllers;

import com.store.user.config.TestSecurityConfig;
import com.store.user.exceptions.UserNotFoundException;
import com.store.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void userNotFoundTest() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(userService.getUserById(userId.toString()))
                .thenThrow(new UserNotFoundException("User with id " + userId + " not found"));

        mockMvc.perform(get("/api/user/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with id " + userId + " not found")
                );
    }

    @Test
    void generalExceptionTest() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(userService.getUserById(userId.toString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/user/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
