package com.iprody.userprofile.service;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.domain.UserDetails;
import com.iprody.userprofile.repository.UserDetailsRepository;
import com.iprody.userprofile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Test
    void shouldUpdateUserDetails() {
        User user = new User();
        user.setId(1L);
        UserDetails existingUserDetails = new UserDetails();
        existingUserDetails.setTelegramId("oldTelegramId");
        existingUserDetails.setMobilePhone("oldMobilePhone");
        user.setUserDetails(existingUserDetails);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDetails updatedUserDetails = new UserDetails();
        updatedUserDetails.setTelegramId("newTelegramId");
        updatedUserDetails.setMobilePhone("newMobilePhone");

        when(userDetailsRepository.saveAndFlush(existingUserDetails)).thenReturn(existingUserDetails);


        UserDetails result = userDetailsService.updateUserDetails(user.getId(), updatedUserDetails);


        assertEquals(updatedUserDetails.getTelegramId(), result.getTelegramId());
        assertEquals(updatedUserDetails.getMobilePhone(), result.getMobilePhone());
    }

    @Test
    void shouldFindUserDetails() {
        User user = new User();
        user.setId(1L);
        UserDetails userDetails = new UserDetails();
        userDetails.setTelegramId("telegramId");
        userDetails.setMobilePhone("mobilePhone");
        user.setUserDetails(userDetails);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<UserDetails> result = userDetailsService.findUserDetails(user.getId());

        assertEquals(userDetails, result.get());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFoundForDetails() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userDetailsService.findUserDetails(42L));
    }

    @Test
    void shouldUpdateUserDetailsNotFound() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userDetailsService.updateUserDetails(42L, new UserDetails()));
    }
}