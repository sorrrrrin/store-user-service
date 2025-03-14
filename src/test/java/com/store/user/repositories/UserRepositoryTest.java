package com.store.user.repositories;

import com.store.user.entities.User;
import com.store.user.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email(TestConstants.USER_EMAIL)
                .username(TestConstants.USER_USERNAME)
                .build();
        userRepository.save(user);
    }

    @Test
    void findByUsernameTest() {
        Optional<User> user = userRepository.findByUsername(TestConstants.USER_USERNAME);
        assertThat(user.isPresent()).isEqualTo(true);
        assertThat(user.get().getUsername()).isEqualTo(TestConstants.USER_USERNAME);
    }
}