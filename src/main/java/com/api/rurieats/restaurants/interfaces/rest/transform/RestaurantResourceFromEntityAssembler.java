package com.api.rurieats.restaurants.interfaces.rest.transform;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.interfaces.rest.resources.RestaurantResource;

public class RestaurantResourceFromEntityAssembler {
    public static RestaurantResource toResourceFromEntity(Restaurant entity) {
        return new RestaurantResource(
                entity.getId(),
                entity.getOwnerId().profileId(),
                entity.getName(),
                entity.getAddress(),
                entity.getAverageRating()
        );
    }
}
