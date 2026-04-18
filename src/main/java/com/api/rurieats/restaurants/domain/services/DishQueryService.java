package com.api.rurieats.restaurants.domain.services;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import com.api.rurieats.restaurants.domain.model.queries.GetDishByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetDishesByRestaurantIdQuery;

import java.util.List;
import java.util.Optional;

public interface DishQueryService {
    Optional<Dish> handle(GetDishByIdQuery query);
    List<Dish> handle(GetDishesByRestaurantIdQuery query);
}
