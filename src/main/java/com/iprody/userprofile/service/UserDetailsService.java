package com.iprody.userprofile.service;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.domain.UserDetails;
import com.iprody.userprofile.repository.UserDetailsRepository;
import com.iprody.userprofile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserDetails updateUserDetails(Long userId, UserDetails userDetails) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found"));
        UserDetails existingUserDetails = user.getUserDetails();
        existingUserDetails.setTelegramId(userDetails.getTelegramId());
        existingUserDetails.setMobilePhone(userDetails.getMobilePhone());
        return userDetailsRepository.saveAndFlush(existingUserDetails);
    }

    public Optional<UserDetails> findUserDetails(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found"));
        return Optional.ofNullable(user.getUserDetails());
    }
}
