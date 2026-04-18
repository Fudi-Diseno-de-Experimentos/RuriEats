package com.api.rurieats.profiles.interfaces.rest.transform;

import com.api.rurieats.profiles.domain.model.commands.CreateProfileCommand;
import com.api.rurieats.profiles.interfaces.rest.resources.CreateProfileResource;

/**
 * Assembler to convert a CreateProfileResource to a CreateProfileCommand.
 */
public class CreateProfileCommandFromResourceAssembler {
    /**
     * Converts a CreateProfileResource to a CreateProfileCommand.
     * @param resource The {@link CreateProfileResource} resource to convert.
     * @return The {@link CreateProfileCommand} command.
     */
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.avtarUrl());
    }
}
