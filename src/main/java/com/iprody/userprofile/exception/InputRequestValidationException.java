package com.iprody.userprofile.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InputRequestValidationException extends RuntimeException {
    private final List<String> validationErrors;

    public InputRequestValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public InputRequestValidationException(List<String> validationErrors) {
        this("Validation error", validationErrors);
    }
}
