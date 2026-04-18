package com.api.rurieats.shared.domain.model.valueobjects;

import java.util.UUID;

public record ProfileId(UUID profileId) {
    public ProfileId {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID cannot be null");
        }
    }
}
