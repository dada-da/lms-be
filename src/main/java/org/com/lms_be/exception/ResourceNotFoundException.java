package org.com.lms_be.exception;

import jakarta.validation.constraints.NotBlank;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with id: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String user, @NotBlank(message = "User ID cannot be blank") String userId) {
    }
}