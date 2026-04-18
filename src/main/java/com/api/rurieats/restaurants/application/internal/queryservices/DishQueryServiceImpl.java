package com.api.rurieats.restaurants.application.internal.queryservices;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import com.api.rurieats.restaurants.domain.model.queries.GetDishByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetDishesByRestaurantIdQuery;
import com.api.rurieats.restaurants.domain.services.DishQueryService;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishQueryServiceImpl implements DishQueryService {

    private final DishRepository dishRepository;

    public DishQueryServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public Optional<Dish> handle(GetDishByIdQuery query) {
        return dishRepository.findById(query.id());
    }

    @Override
    public List<Dish> handle(GetDishesByRestaurantIdQuery query) {
        return dishRepository.findAllByRestaurantId(query.restaurantId());
    }
}
