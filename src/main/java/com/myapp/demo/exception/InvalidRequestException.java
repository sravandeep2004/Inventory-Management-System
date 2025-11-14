package com.myapp.demo.exception;

/**
 * Exception thrown for invalid requests (validation errors, etc.)
 * Should result in HTTP 400 response
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
