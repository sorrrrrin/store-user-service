package com.store.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.user.config.TestSecurityConfig;
import com.store.user.dtos.UserDto;
import com.store.user.services.UserService;
import com.store.user.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = TestUtils.getUserDto();
    }

    @Test
    void getUsersTest() throws Exception {
        List<UserDto> users = Collections.singletonList(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(userDto.getId()));
    }

    @Test
    void addUserTest() throws Exception {
        when(userService.addUser(any(UserDto.class))).thenReturn(userDto);

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(anyString(), any(UserDto.class))).thenReturn(userDto);

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }

    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(any(UserDto.class));

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(delete("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllUsersTest() throws Exception {
        doNothing().when(userService).deleteAllUsers();

        mockMvc.perform(delete("/api/user/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.getUserById(userDto.getId())).thenReturn(userDto);

        mockMvc.perform(get("/api/user/users/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }
}