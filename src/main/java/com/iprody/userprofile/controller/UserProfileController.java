package com.iprody.userprofile.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iprody.userprofile.dto.UserRequest;
import com.iprody.userprofile.dto.UserResponse;
import com.iprody.userprofile.mapper.UserMapper;
import com.iprody.userprofile.service.UserService;
import com.iprody.userprofile.dto.UserDetailsRequest;
import com.iprody.userprofile.service.UserDetailsService;
import com.iprody.userprofile.repository.UserSpecification;

import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "user profiles", description = "API for managing user profiles")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    private UserMapper userMapper;

    @Operation(summary = "Create a new user", description = "Create a new user with specified details")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Validated @RequestBody UserRequest userRequest) {
        log.info("Add user: firstName: {}, lastName: {}, email: {}", userRequest.getFirstName(),
                userRequest.getLastName(), userRequest.getEmail());
        return userMapper.userToUserResponse(userService.addUser(userMapper.userRequestToUser(userRequest)));
    }

    @Operation(summary = "Update user by ID", description = "Update user by provided ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse updateUser(@PathVariable Long id, @Validated @RequestBody UserRequest userRequest) {
        log.info("Update user with ID: {}", id);
        return userMapper.userToUserResponse(userService.updateUser(id, userMapper.userRequestToUser(userRequest)));
    }

    @Operation(summary = "Update user details by ID", description = "Update additional details of the user by the ID")
    @PutMapping("/{id}/details")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse updateUserDetails(@PathVariable Long id, @Validated @RequestBody UserDetailsRequest userDetailsRequest) {
        log.info("Update user details with ID: {}", id);
        return userMapper.userDetailsToUserResponse(userDetailsService.updateUserDetails(id,
                userMapper.userDetailsRequestToUserDetails(userDetailsRequest)));
    }

    @Operation(summary = "Find user by name", description = "Finds users with the specified first name")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> findUserWithDetails(@RequestParam(required = false) String firstName,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) String mobilePhone,
                                                  @RequestParam(required = false) String telegramId) {
        log.info("Find users with specified details");

        UserSpecification userSpecification = new UserSpecification()
                .firstNameContains(firstName)
                .lastNameContains(lastName)
                .emailContains(email)
                .mobilePhoneContains(mobilePhone)
                .telegramIdContains(telegramId);
        return userService.findUsersWithDetails(userSpecification).stream().map(userMapper::userToUserResponse).toList();
    }
}
