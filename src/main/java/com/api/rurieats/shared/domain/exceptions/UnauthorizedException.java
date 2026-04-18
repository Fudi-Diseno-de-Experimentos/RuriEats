package com.api.rurieats.shared.domain.exceptions;

/**
 * Exception thrown when user is not authorized to perform an action
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("No tienes permisos para realizar esta acción");
    }
}
