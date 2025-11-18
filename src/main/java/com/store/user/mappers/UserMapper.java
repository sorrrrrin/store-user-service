package com.store.user.mappers;

import com.store.user.dtos.UserDto;
import com.store.user.dtos.UserResponseDto;
import com.store.user.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    UserResponseDto userToUserResponseDto(User user);

}