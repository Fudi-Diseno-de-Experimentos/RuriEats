package com.api.rurieats.posts.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record CommentId(UUID id) {
    public CommentId() {
        this(UUID.randomUUID());
    }
}
