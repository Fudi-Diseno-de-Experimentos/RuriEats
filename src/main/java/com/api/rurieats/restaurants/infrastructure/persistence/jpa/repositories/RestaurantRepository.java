package com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findByOwnerId(ProfileId ownerId);
}
