package com.iprody.userprofile.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.userprofile.dto.UserRequest;
import com.iprody.userprofile.exception.ResourceNotFoundException;
import com.iprody.userprofile.exception.ResourceProcessingException;
import com.iprody.userprofile.mapper.UserMapper;
import com.iprody.userprofile.service.UserDetailsService;
import com.iprody.userprofile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWebMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserMapper userMapper;

    private UserRequest userRequest;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        userRequest = UserRequest.builder()
                .firstName("Elon")
                .lastName("Musk")
                .email("elon.m@gmail.com")
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnBadRequestWhenAddUserWithInvalidData() throws Exception {
        userRequest.setFirstName("");
        userRequest.setLastName("");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void shouldReturnNotFoundWhenUpdateNonExistingUser() throws Exception {
        when(userMapper.userRequestToUser(any(UserRequest.class))).thenThrow(new ResourceNotFoundException("User with ID 1 not found."));
        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User with ID 1 not found."));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateUserDetailsWithInvalidData() throws Exception {
        UserRequest invalidUserRequest = UserRequest.builder().build();

        mockMvc.perform(put("/user/1/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        // Prepare an invalid request body
        String invalidRequestBody = "{ \"firstName\": \"\", \"lastName\": \"\", \"email\": \"invalidemail\" }";

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Error"))
                .andExpect(jsonPath("$.details").isArray());
    }
    @Test
    void shouldHandleBadRequestWhenInvalidJson() throws Exception {
        String invalidJson = "{ \" firstName\": \"\", \"lastName\": \"\" \"email\": \"invalidemail\" }";

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid JSON"));
    }

    @Test
    void shouldHandleResourceProcessingException() throws Exception {

        when(userMapper.userRequestToUser(any(UserRequest.class)))
                .thenThrow(new ResourceProcessingException("Error occurred while adding user."));

        // Prepare a valid user request
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Error occurred while adding user."));
    }

    @Test
    void shouldHandleException() throws Exception {
        // Mock the service to throw a RuntimeException
        when(userService.findUser(anyLong())).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Internal Server Error"));
    }
}
