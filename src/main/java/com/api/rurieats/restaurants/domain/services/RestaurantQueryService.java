package com.api.rurieats.restaurants.domain.services;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByOwnerIdQuery;

import java.util.Optional;

public interface RestaurantQueryService {
    Optional<Restaurant> handle(GetRestaurantByIdQuery query);
    Optional<Restaurant> handle(GetRestaurantByOwnerIdQuery query);
}
