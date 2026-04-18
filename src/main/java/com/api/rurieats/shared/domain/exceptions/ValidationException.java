package com.api.rurieats.shared.domain.exceptions;

/**
 * Exception thrown when business validation fails
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
}
