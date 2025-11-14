package com.myapp.demo.exception;

/**
 * Exception thrown when a requested item is not found
 * Should result in HTTP 404 response
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
