package com.api.rurieats.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Resource for creating a profile.
 */
@Schema(description = "Request to create a new profile")
public record CreateProfileResource(
        @Schema(description = "User ID from IAM context", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        @NotNull(message = "User ID cannot be null")
        UUID userId,

        @Schema(description = "First name", example = "Kevin", required = true)
        @NotBlank(message = "First name cannot be blank")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String firstName,

        @Schema(description = "Last name", example = "Moreira", required = true)
        @NotBlank(message = "Last name cannot be blank")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Schema(description = "Avatar URL", example = "https://i.pinimg.com/736x/27/04/39/2704399f46a1ac9a1d353e59a91dfe19.jpg")
        String avtarUrl

        ) {


    /**
     * Validates the resource.
     *
     * @throws IllegalArgumentException if the resource is invalid.
     */
    public CreateProfileResource {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
        if (avtarUrl == null || avtarUrl.isBlank()) throw new IllegalArgumentException("avtarUrl is required");

       }
}
