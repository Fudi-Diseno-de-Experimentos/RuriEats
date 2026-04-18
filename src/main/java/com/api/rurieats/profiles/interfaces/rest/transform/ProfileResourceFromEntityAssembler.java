package com.api.rurieats.profiles.interfaces.rest.transform;

import com.api.rurieats.profiles.domain.model.aggregates.Profile;
import com.api.rurieats.profiles.domain.model.valueobjects.UserId;
import com.api.rurieats.profiles.interfaces.rest.resources.ProfileResource;

/**
 * Assembler to convert a Profile entity to a ProfileResource.
 */
public class ProfileResourceFromEntityAssembler {
    /**
     * Converts a Profile entity to a ProfileResource.
     * @param entity The {@link Profile} entity to convert.
     * @return The {@link ProfileResource} resource.
     */
    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
                entity.getId(),
                entity.getUserId().userId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAvatarUrl());
    }
}
