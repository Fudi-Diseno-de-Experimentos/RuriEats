package com.api.rurieats.posts.domain.model.queries;

import java.util.UUID;

public record GetAllPostsByRestaurantIdQuery(UUID restaurantId) {
}
