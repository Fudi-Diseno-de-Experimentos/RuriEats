package com.api.rurieats.restaurants.interfaces.acl;

import java.util.UUID;

public interface RestaurantsContextFacade {
    boolean existsRestaurantById(UUID restaurantId);
    boolean existsDishById(UUID dishId);
}
