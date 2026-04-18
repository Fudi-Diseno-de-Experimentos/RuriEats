package com.api.rurieats.posts.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record PostId(UUID id) {
    public PostId() {
        this(UUID.randomUUID());
    }
}
