package com.api.rurieats.posts.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record RestaurantId(UUID id) {
}
