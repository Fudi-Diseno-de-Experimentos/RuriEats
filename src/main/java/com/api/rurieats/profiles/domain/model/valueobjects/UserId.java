package com.api.rurieats.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record UserId(UUID userId) {
    public UserId {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}