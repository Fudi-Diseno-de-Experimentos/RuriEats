package com.api.rurieats.shared.domain.model.events;

import java.util.UUID;

public record DishCreatedEvent(
        UUID dishId,
        UUID restaurantId,
        UUID ownerProfileId,
        String dishName,
        String imageUrl
) {}