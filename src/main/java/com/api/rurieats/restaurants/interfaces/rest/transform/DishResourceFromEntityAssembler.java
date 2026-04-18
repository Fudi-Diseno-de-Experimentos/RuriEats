package com.api.rurieats.restaurants.interfaces.rest.transform;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import com.api.rurieats.restaurants.interfaces.rest.resources.DishResource;

public class DishResourceFromEntityAssembler {
    public static DishResource toResourceFromEntity(Dish entity) {
        return new DishResource(
                entity.getId(),
                entity.getRestaurant().getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice().amount(),
                entity.getPrice().currency(),
                entity.getIngredients(),
                entity.getImageUrl()
        );
    }
}
