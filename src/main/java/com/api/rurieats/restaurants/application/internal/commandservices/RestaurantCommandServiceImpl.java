package com.api.rurieats.restaurants.application.internal.commandservices;

import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.domain.model.commands.CreateRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateRestaurantCommand;
import com.api.rurieats.restaurants.domain.services.RestaurantCommandService;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantCommandServiceImpl implements RestaurantCommandService {

    private final RestaurantRepository restaurantRepository;
    private final ProfilesContextFacade profilesContextFacade;

    public RestaurantCommandServiceImpl(RestaurantRepository restaurantRepository, ProfilesContextFacade profilesContextFacade) {
        this.restaurantRepository = restaurantRepository;
        this.profilesContextFacade = profilesContextFacade;
    }

    @Override
    public Optional<Restaurant> handle(CreateRestaurantCommand command) {
        if (!profilesContextFacade.existsProfileById(command.ownerId().profileId())) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        
        // Optional: Check if owner already has a restaurant if it's 1:1, but assuming 1:N for now or we just save.
        Restaurant restaurant = new Restaurant(command.ownerId(), command.name(), command.address());
        return Optional.of(restaurantRepository.save(restaurant));
    }

    @Override
    public Optional<Restaurant> handle(UpdateRestaurantCommand command) {
        return restaurantRepository.findById(command.id()).map(restaurant -> {
            restaurant.update(command.name(), command.address());
            return restaurantRepository.save(restaurant);
        });
    }

    @Override
    public void handle(DeleteRestaurantCommand command) {
        restaurantRepository.findById(command.id()).ifPresent(restaurantRepository::delete);
    }
}
