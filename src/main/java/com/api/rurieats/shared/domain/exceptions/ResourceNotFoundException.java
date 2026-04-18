package com.api.rurieats.shared.domain.exceptions;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s con ID '%s' no encontrado", resourceName, resourceId));
    }
}
