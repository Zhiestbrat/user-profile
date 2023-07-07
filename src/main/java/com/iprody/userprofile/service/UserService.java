package com.iprody.userprofile.service;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.repository.UserRepository;
import com.iprody.userprofile.repository.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + id + " not found"));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return userRepository.saveAndFlush(existingUser);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No user with id " + id));
    }

    public List<User> findUsersWithDetails(UserSpecification userSpec) {
        return userRepository.findAll(Specification.where(userSpec.getFilters()));
    }
}
