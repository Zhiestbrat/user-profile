package com.iprody.userprofile.service;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User addUser(User user) {
        user.setId(null);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("User with id " + user.getId() + " not found"));
        return userRepository.saveAndFlush(existingUser);
    }


    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }
}
