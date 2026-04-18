package com.api.rurieats.restaurants.domain.services;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.domain.model.commands.CreateRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateRestaurantCommand;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantCommandService {
    Optional<Restaurant> handle(CreateRestaurantCommand command);
    Optional<Restaurant> handle(UpdateRestaurantCommand command);
    void handle(DeleteRestaurantCommand command);
}
