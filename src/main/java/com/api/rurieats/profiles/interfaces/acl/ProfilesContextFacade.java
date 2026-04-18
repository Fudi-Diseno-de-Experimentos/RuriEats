package com.api.rurieats.profiles.interfaces.acl;

import java.util.UUID;

/**
 * ProfilesContextFacade
 */
public interface ProfilesContextFacade {
    /**
     * Create a new profile
     * @param userId The user ID
     * @param firstName The first name
     * @param lastName The last name
     * @param avatarUrl The avatar URL
     * @return The profile ID
     */
    UUID createProfile(UUID userId, String firstName, String lastName, String avatarUrl);

    /**
     * Check if a profile exists by ID
     * @param profileId The profile ID
     * @return true if the profile exists, false otherwise
     */
    boolean existsProfileById(UUID profileId);

    /**
     * Get the user ID associated with a profile ID
     * @param profileId The profile ID
     * @return The user ID or null if not found
     */
    UUID getUserIdByProfileId(UUID profileId);

}
