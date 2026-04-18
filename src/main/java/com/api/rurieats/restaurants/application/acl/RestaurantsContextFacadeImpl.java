package com.api.rurieats.restaurants.application.acl;

import com.api.rurieats.restaurants.domain.model.queries.GetDishByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByIdQuery;
import com.api.rurieats.restaurants.domain.services.DishQueryService;
import com.api.rurieats.restaurants.domain.services.RestaurantQueryService;
import com.api.rurieats.restaurants.interfaces.acl.RestaurantsContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantsContextFacadeImpl implements RestaurantsContextFacade {

    private final RestaurantQueryService restaurantQueryService;
    private final DishQueryService dishQueryService;

    public RestaurantsContextFacadeImpl(RestaurantQueryService restaurantQueryService, DishQueryService dishQueryService) {
        this.restaurantQueryService = restaurantQueryService;
        this.dishQueryService = dishQueryService;
    }

    @Override
    public boolean existsRestaurantById(UUID restaurantId) {
        var query = new GetRestaurantByIdQuery(restaurantId);
        return restaurantQueryService.handle(query).isPresent();
    }

    @Override
    public boolean existsDishById(UUID dishId) {
        var query = new GetDishByIdQuery(dishId);
        return dishQueryService.handle(query).isPresent();
    }
}
