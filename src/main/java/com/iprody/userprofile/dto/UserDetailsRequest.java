package com.iprody.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsRequest {

    @Pattern(regexp = "^\\d{10}$")
    private String mobilePhone;

    @NotEmpty
    @NotBlank
    @Pattern(regexp = "@[a-zA-Z0-9]+(?:[_\\-][a-zA-Z0-9]*)*")
    private String telegramId;
}
