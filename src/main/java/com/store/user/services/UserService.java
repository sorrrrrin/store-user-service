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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Value("${spring.kafka.topic}")
    private String topic;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream()
                        .map(authority -> (GrantedAuthority) authority)
                        .collect(Collectors.toList())
        );
    }

    public List<UserDto> getAllUsers() {
        log.debug("Fetching all users.");
        return userRepository.findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    public UserDto addUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

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
