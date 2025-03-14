package com.store.user.mappers;

import com.store.user.dtos.UserDto;
import com.store.user.entities.User;
import com.store.user.utils.TestConstants;
import com.store.user.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void userToUserDtoTest() {
        User user = TestUtils.getUser();

        UserDto userDto = userMapper.userToUserDto(user);

        assertNotNull(userDto);
        assertEquals(TestConstants.USER_ID, userDto.getId());
        assertEquals(TestConstants.USER_EMAIL, userDto.getEmail());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void userToUserDto_NullUser_ReturnsNullTest() {
        UserDto userDto = userMapper.userToUserDto(null);
        assertNull(userDto);
    }

    @Test
    void userDtoToUserTest() {
        UserDto userDto = TestUtils.getUserDto();

        User user = userMapper.userDtoToUser(userDto);

        assertNotNull(user);
        assertEquals(TestConstants.USER_ID, user.getId());
        assertEquals(TestConstants.USER_EMAIL, user.getEmail());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void userDtoToUser_NullUserDto_ReturnsNullTest() {
        User user = userMapper.userDtoToUser(null);
        assertNull(user);
    }
}