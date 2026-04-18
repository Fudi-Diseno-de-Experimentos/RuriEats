package com.api.rurieats.profiles.interfaces.rest.transform;

import com.api.rurieats.profiles.domain.model.commands.UpdateProfileCommand;
import com.api.rurieats.profiles.interfaces.rest.resources.UpdateProfileResource;

import java.util.UUID;

public class UpdateProfileCommandFromResourceAssembler {
    /**
     * Converts an update profile resource to an update profile command.
     * @param profileId the profile ID.
     * @param resource the {@link UpdateProfileResource} resource.
     * @return the {@link UpdateProfileCommand} command.
     */
    public static UpdateProfileCommand toCommandFromResource(UUID profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                profileId,
                resource.firstName(),
                resource.lastName(),
                resource.avatarUrl());
    }
}
