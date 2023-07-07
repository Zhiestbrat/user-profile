package com.iprody.userprofile.repository;

import com.iprody.userprofile.domain.UserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
public class UserDetailsRepositoryTest {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry registry) {
        POSTGRES_CONTAINER.start();
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void shouldGenerateId() {
        UserDetails userDetails = UserDetails.builder()
                .telegramId("@testId")
                .mobilePhone("2345678900")
                .build();
        UserDetails saveUserDetails = userDetailsRepository.save(userDetails);
        assertThat(saveUserDetails.getId()).isNotNull();
    }

    @Test
    void shouldReturnSavedUser() {
        UserDetails saveUserDetails = userDetailsRepository.save(UserDetails.builder()
                .telegramId("@youBetterPassTheTest")
                .mobilePhone("380631234567")
                .build());
        Long userDetailsId = saveUserDetails.getId();
        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findById(userDetailsId);
        assertThat(userDetailsOptional).isPresent();
    }
}