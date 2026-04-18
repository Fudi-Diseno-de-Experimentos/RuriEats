package com.api.rurieats.restaurants.application.internal.queryservices;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByOwnerIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetAllRestaurantsQuery;
import com.api.rurieats.restaurants.domain.services.RestaurantQueryService;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantQueryServiceImpl implements RestaurantQueryService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantQueryServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Optional<Restaurant> handle(GetRestaurantByIdQuery query) {
        return restaurantRepository.findById(query.id());
    }

    @Override
    public Optional<Restaurant> handle(GetRestaurantByOwnerIdQuery query) {
        return restaurantRepository.findByOwnerId(query.ownerId());
    }

    @Override
    public Page<Restaurant> handle(GetAllRestaurantsQuery query) {
        return restaurantRepository.findAll(query.pageable());
    }
}
