package com.api.rurieats.profiles.domain.model.queries;

import com.api.rurieats.profiles.domain.model.valueobjects.UserId;

/**
 * Get profile by user ID query
 * Query to retrieve a profile by its associated user ID
 */
public record GetProfileByUserIdQuery(UserId userId) {
    public GetProfileByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}