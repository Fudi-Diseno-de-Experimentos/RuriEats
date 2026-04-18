package com.api.rurieats.profiles.domain.services;

import com.api.rurieats.profiles.domain.model.aggregates.Profile;
import com.api.rurieats.profiles.domain.model.queries.GetAllProfilesQuery;

import com.api.rurieats.profiles.domain.model.queries.GetProfileByIdQuery;
import com.api.rurieats.profiles.domain.model.queries.GetProfileByUserIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Profile Query Service
 */
public interface ProfileQueryService {
    /**
     * Handle get profile by ID query
     * @param query the get profile by ID query
     * @return the profile if found
     */
    Optional<Profile> handle(GetProfileByIdQuery query);

    /**
     * Handle get profile by user ID query
     * @param query the get profile by user ID query
     * @return the profile if found
     */
    Optional<Profile> handle(GetProfileByUserIdQuery query);

    /**
     * Handle get all profiles query
     * @param query the get all profiles query
     * @return list of all profiles
     */
    List<Profile> handle(GetAllProfilesQuery query);
}
