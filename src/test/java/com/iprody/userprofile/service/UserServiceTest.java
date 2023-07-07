package com.iprody.userprofile.service;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldAddUser() {
        User user = User.builder()
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();

        when(userRepository.saveAndFlush(user)).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });

        User returnedUser = userService.addUser(user);

        assertNotNull(returnedUser.getId());
        assertEquals(2L, returnedUser.getId());
    }

    @Test
    void shouldFindUserById() {
        User user = User.builder()
                .id(1L)
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.findUser(user.getId());

        assertEquals(user, foundUser);
    }

    @Test
    void shouldFindUser() {
        User user = User.builder()
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User result = userService.findUser(user.getId());

        assertEquals(user, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        User existingUser = User.builder()
                .id(133L)
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findUser(133L));
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = User.builder()
                .id(1L)
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.saveAndFlush(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, User.builder()
                .id(1L)
                .firstName("iProdyUser")
                .lastName("iProdyPass")
                .email("user@iprody.com")
                .build());

        assertThat(updatedUser.getFirstName()).isEqualTo("iProdyUser");
        assertThat(updatedUser.getLastName()).isEqualTo("iProdyPass");
    }
}