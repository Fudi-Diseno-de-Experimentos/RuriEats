package com.api.rurieats.profiles.domain.services;

import com.api.rurieats.profiles.domain.model.aggregates.Profile;
import com.api.rurieats.profiles.domain.model.commands.CreateProfileCommand;
import com.api.rurieats.profiles.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

/**
 * Profile Command Service
 */
public interface ProfileCommandService {
    /**
     * Handle Create Profile Command
     *
     * @param command The {@link CreateProfileCommand} Command
     * @return A {@link Profile} instance if the command is valid, otherwise empty
     * @throws IllegalArgumentException if the email address already exists
     */
    Optional<Profile> handle(CreateProfileCommand command);

    /**
     * Handle update profile command
     * @param command the update profile command
     * @return the updated profile
     */
    Optional<Profile> handle(UpdateProfileCommand command);

}
