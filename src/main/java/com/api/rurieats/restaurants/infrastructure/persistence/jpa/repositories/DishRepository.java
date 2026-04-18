package com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
    List<Dish> findAllByRestaurantId(UUID restaurantId);
}
