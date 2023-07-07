package com.iprody.userprofile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.domain.UserDetails;
import com.iprody.userprofile.dto.UserDetailsRequest;
import com.iprody.userprofile.dto.UserRequest;
import com.iprody.userprofile.dto.UserResponse;
import com.iprody.userprofile.mapper.UserMapper;
import com.iprody.userprofile.repository.UserSpecification;
import com.iprody.userprofile.service.UserDetailsService;
import com.iprody.userprofile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWebMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDetails userDetails;
    private UserRequest userRequest;
    private UserResponse userResponse;
    private UserDetailsRequest userDetailsRequest;

    // These are especially to ensure proper functionality of updateUser method
    private User updatedUser;
    private UserDetails updatedUserDetails;
    private UserRequest updatedUserRequest;
    private UserResponse updatedUserResponse;
    private UserDetailsRequest updatedUserDetailsRequest;
    private UserResponse userWithUpdatedDetails;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userRequest = UserRequest.builder()
                .firstName("Elon")
                .lastName("Musk")
                .email("elon.m@gmail.com")
                .build();

        userDetails = UserDetails.builder()
                .mobilePhone("2345678900")
                .telegramId("@musk")
                .build();

        user = User.builder()
                .id(1L)
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .userDetails(userDetails)
                .build();

        userResponse = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobilePhone(user.getUserDetails().getMobilePhone())
                .telegramId(user.getUserDetails().getTelegramId())
                .build();

        userDetailsRequest = UserDetailsRequest.builder()
                .telegramId(userDetails.getTelegramId())
                .mobilePhone(userDetails.getMobilePhone())
                .build();

        updatedUserRequest = UserRequest.builder()
                .firstName("Steve")
                .lastName("Jobs")
                .email("steve.j@apple.com")
                .build();

        updatedUserDetails = UserDetails.builder()
                .mobilePhone("2355678900")
                .telegramId("@jobs")
                .user(user)
                .build();

        updatedUser = User.builder()
                .id(1L)
                .firstName(updatedUserRequest.getFirstName())
                .lastName(updatedUserRequest.getLastName())
                .email(updatedUserRequest.getEmail())
                .userDetails(updatedUserDetails)
                .build();

        userWithUpdatedDetails = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .telegramId(updatedUserDetails.getTelegramId())
                .mobilePhone(updatedUserDetails.getMobilePhone())
                .build();

        updatedUserResponse = UserResponse.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .mobilePhone(updatedUserDetails.getMobilePhone())
                .telegramId(updatedUserDetails.getTelegramId())
                .build();

        updatedUserDetailsRequest = UserDetailsRequest.builder()
                .telegramId(updatedUserDetails.getTelegramId())
                .mobilePhone(updatedUserDetails.getMobilePhone())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Add user")
    public void shouldAddUser() throws Exception {
        when(userService.addUser(user)).thenReturn(user);
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(userService, times(1)).addUser(user);
        verify(userMapper, times(1)).userRequestToUser(userRequest);
        verify(userMapper, times(1)).userToUserResponse(user);
        verifyNoMoreInteractions(userService, userMapper);
    }

    @Test
    @DisplayName("Update user")
    void shouldUpdateUser() throws Exception {
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(updatedUser);
        when(userMapper.userRequestToUser(any(UserRequest.class))).thenReturn(updatedUser);
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(updatedUserResponse);

        mockMvc.perform(put("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName").value("Steve"))
                .andExpect(jsonPath("$.lastName").value("Jobs"))
                .andExpect(jsonPath("$.email").value("steve.j@apple.com"));

        verify(userService, times(1)).updateUser(any(Long.class), any(User.class));
        verify(userMapper, times(1)).userRequestToUser(any(UserRequest.class));
        verify(userMapper, times(1)).userToUserResponse(any(User.class));
        verifyNoMoreInteractions(userService, userMapper);
    }

    @Test
    @DisplayName("Update user details")
    void shouldUpdateUserDetails() throws Exception {
        when(userDetailsService.updateUserDetails(eq(1L), any(UserDetails.class))).thenReturn(updatedUserDetails);
        when(userMapper.userDetailsRequestToUserDetails(any(UserDetailsRequest.class))).thenReturn(updatedUserDetails);
        when(userMapper.userDetailsToUserResponse(any(UserDetails.class))).thenReturn(userWithUpdatedDetails);

        mockMvc.perform(put("/user/{id}/details", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserResponse))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName").value("Elon"))
                .andExpect(jsonPath("$.lastName").value("Musk"))
                .andExpect(jsonPath("$.email").value("elon.m@gmail.com"))
                .andExpect(jsonPath("$.mobilePhone").value("2355678900"))
                .andExpect(jsonPath("$.telegramId").value("@jobs"));

        verify(userDetailsService, times(1)).updateUserDetails(eq(1L),
                any(UserDetails.class));
        verifyNoMoreInteractions(userDetailsService);
    }

    @Test
    @DisplayName("Find user with details")
    public void testFindUserWithDetails() throws Exception {

        List<User> users = List.of(user);

        when(userService.findUsersWithDetails(any(UserSpecification.class))).thenReturn(users);
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(userResponse);

        mockMvc.perform(get("/user")
                        .param("firstName", "Elon")
                        .param("lastName", "Musk")
                        .param("email", "elon.m@gmail.com")
                        .param("mobilePhone", "2345678900")
                        .param("telegramId", "@musk")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("Elon"));

        verify(userService, times(1)).findUsersWithDetails(any(UserSpecification.class));
        verify(userMapper, times(1)).userToUserResponse(any(User.class));
        verifyNoMoreInteractions(userService, userMapper);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserRequests")
    @DisplayName("Add user with invalid data")
    void shouldNotAddUserWithInvalidData(String content, int expectedStatus) throws Exception {
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Arguments> provideInvalidUserRequests() {
        UserRequest wrongDataRequest = UserRequest.builder()
                .firstName("")
                .lastName("")
                .build();

        UserRequest incorrectEmailRequest = UserRequest.builder()
                .firstName("Elon")
                .lastName("Musk")
                .email("asdasd")
                .build();

        String wrongFormatData = "{ \"firstName\": \"Elon\", \"lastName\": \"Musk\", \"email\": \"abc\" }";
        String missingFieldData = "{ \"firstName\": \"Elon\", \"lastName\": \"Musk\" }";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return Stream.of(
                    Arguments.of(objectMapper.writeValueAsString(wrongDataRequest), HttpStatus.BAD_REQUEST.value()),
                    Arguments.of(objectMapper.writeValueAsString(incorrectEmailRequest), HttpStatus.BAD_REQUEST.value()),
                    Arguments.of(wrongFormatData, HttpStatus.BAD_REQUEST.value()),
                    Arguments.of(missingFieldData, HttpStatus.BAD_REQUEST.value())
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
}