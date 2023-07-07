package com.iprody.userprofile.mapper;

import com.iprody.userprofile.domain.User;
import com.iprody.userprofile.domain.UserDetails;
import com.iprody.userprofile.dto.UserDetailsRequest;
import com.iprody.userprofile.dto.UserRequest;
import com.iprody.userprofile.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userDetails.telegramId", target = "telegramId")
    @Mapping(source = "userDetails.mobilePhone", target = "mobilePhone")
    UserResponse userToUserResponse(User user);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    UserResponse userDetailsToUserResponse(UserDetails userDetails);

    User userRequestToUser(UserRequest userRequest);

    UserDetails userDetailsRequestToUserDetails(UserDetailsRequest userDetailsRequest);
}
