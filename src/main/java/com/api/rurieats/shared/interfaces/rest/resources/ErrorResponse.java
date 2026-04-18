package com.api.rurieats.shared.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response with detailed information")
public record ErrorResponse(
        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error message", example = "No se puede crear una cuenta con el mismo nombre")
        String message,

        @Schema(description = "Detailed error description", example = "Ya existe una cuenta con el nombre 'Mi Cuenta Principal'")
        String details,

        @Schema(description = "Timestamp of the error", example = "2026-02-16T22:28:25.141")
        LocalDateTime timestamp,

        @Schema(description = "Request path where the error occurred", example = "/api/v1/accounts")
        String path
) {
    public ErrorResponse(int status, String message, String details, String path) {
        this(status, message, details, LocalDateTime.now(), path);
    }

    public ErrorResponse(int status, String message, String path) {
        this(status, message, null, LocalDateTime.now(), path);
    }
}
