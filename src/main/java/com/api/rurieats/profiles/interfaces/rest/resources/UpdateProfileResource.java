package com.api.rurieats.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing profile")
public record UpdateProfileResource(
        @Schema(description = "First name", example = "Kevin2", required = true)
        @NotBlank(message = "First name cannot be blank")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @Schema(description = "Last name", example = "Moreira2", required = true)
        @NotBlank(message = "Last name cannot be blank")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Schema(description = "Avatar URL", example = "https://i.pinimg.com/736x/27/04/39/2704399f46a1ac9a1d353e59a91dfe19.jpg")
        String avatarUrl

        ) {

}
