package com.api.rurieats.restaurants.domain.services;

import com.api.rurieats.restaurants.domain.model.commands.CreateDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateDishCommand;
import com.api.rurieats.restaurants.domain.model.entities.Dish;

import java.util.Optional;
import java.util.UUID;

public interface DishCommandService {
    Optional<Dish> handle(CreateDishCommand command);
    Optional<Dish> handle(UpdateDishCommand command);
    void handle(DeleteDishCommand command);
}
