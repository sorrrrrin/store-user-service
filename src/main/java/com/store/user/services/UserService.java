package com.store.user.services;

import com.store.user.dtos.UserDto;
import com.store.user.entities.User;
import com.store.user.exceptions.UserNotFoundException;
import com.store.user.mappers.UserMapper;
import com.store.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Value("${spring.kafka.topic}")
    private String topic;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    public UserDto addUser(UserDto userDto) {
        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
    }

    public UserDto updateUser(String id, UserDto userDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        existingUser.setName(userDto.getName());

//        kafkaTemplate.send(topic, userDto.toString());

        return userMapper.userToUserDto(userRepository.save(existingUser));
    }

    public void deleteUser(UserDto userDto) {
        userRepository.delete(userMapper.userDtoToUser(userDto));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
