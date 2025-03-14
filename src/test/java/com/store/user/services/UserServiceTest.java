package com.store.user.services;

import com.store.user.dtos.UserDto;
import com.store.user.entities.User;
import com.store.user.exceptions.UserNotFoundException;
import com.store.user.mappers.UserMapper;
import com.store.user.mappers.UserMapperImpl;
import com.store.user.repositories.UserRepository;
import com.store.user.utils.TestConstants;
import com.store.user.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UserMapper userMapper = new UserMapperImpl();


    @BeforeEach
    void setUp() {
        userService = new UserService(userMapper, userRepository, kafkaTemplate, passwordEncoder);
    }

    @Test
    void getAllUsersTest() {
        User user = TestUtils.getUser();

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(TestConstants.USER_ID, users.get(0).getId());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserByIdTest() {
        User user = TestUtils.getUser();

        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(TestConstants.USER_ID);

        assertNotNull(userDto);
        assertEquals(TestConstants.USER_ID, userDto.getId());
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    void getUserByIdThrowsExceptionWhenNotFound() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(TestConstants.USER_ID));

        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    void addUserTest() {
        UserDto userDto = TestUtils.getUserDto();
        User user = TestUtils.getUser();

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUser = userService.addUser(userDto);

        assertNotNull(savedUser);
        assertEquals(TestConstants.USER_ID, savedUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserTest() {
        UserDto userDto = TestUtils.getUserDto();
        User existingUser = TestUtils.getUser();

        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUser(TestConstants.USER_ID, userDto);

        assertNotNull(updatedUser);
        assertEquals(TestConstants.USER_ID, updatedUser.getId());
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserTest() {
        UserDto userDto = new UserDto();
        userDto.setId(TestConstants.USER_ID);

        userService.deleteUser(userDto);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void deleteAllUsersTest() {
        userService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAll();
    }
}