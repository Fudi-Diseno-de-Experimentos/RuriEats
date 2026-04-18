package com.api.rurieats.shared.domain.exceptions;

/**
 * Exception thrown when attempting to create a resource that already exists
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Ya existe %s con %s '%s'", resourceName, fieldName, fieldValue));
    }
}
