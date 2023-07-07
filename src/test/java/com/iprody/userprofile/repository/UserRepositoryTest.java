package com.iprody.userprofile.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.domain.UserDetails;

@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry registry) {
        POSTGRES_CONTAINER.start();
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGenerateId() {
        User user = User.builder()
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();
        User saveUser = userRepository.save(user);
        assertThat(saveUser.getId()).isNotNull();
    }

    @Test
    void shouldReturnSavedUser() {
        User saveUser = userRepository.save(User.builder()
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user2@iprody.com")
                .build());
        Long userId = saveUser.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();
    }

    @Test
    void shouldReturnSavedUserWithDetails() {
        User saveUser = userRepository.save(User.builder()
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user100@iprody.com")
                .userDetails(
                        UserDetails.builder()
                                .telegramId("@example")
                                .build()
                )
                .build());

        Long userId = saveUser.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();
        assertThat(userOptional.map(User::getUserDetails)).isPresent();
        assertThat(userOptional.map(User::getUserDetails).map(UserDetails::getTelegramId)).hasValue("@example");
    }
}