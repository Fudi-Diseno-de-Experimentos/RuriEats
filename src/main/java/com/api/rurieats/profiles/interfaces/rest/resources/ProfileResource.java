package com.api.rurieats.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Resource for a profile.
 */
@Schema(description = "Profile information")
public record ProfileResource(
        @Schema(description = "Profile ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID profileId,
        @Schema(description = "User ID from IAM context", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID userId,
        @Schema(description = "First name", example = "Kevin")
        String firstName,
        @Schema(description = "Last name", example = "Moreira")
        String lastName,
        @Schema(description = "Avatar URL", example = "https://i.pinimg.com/736x/27/04/39/2704399f46a1ac9a1d353e59a91dfe19.jpg")
        String avtarUrl
        ) {
}
