package com.iprody.userprofile.exception.api;


import java.util.List;


public record ApiError(int status, String message, List<String> details) {
    public ApiError(int status, String message) {
        this(status, message, null);
    }
}
