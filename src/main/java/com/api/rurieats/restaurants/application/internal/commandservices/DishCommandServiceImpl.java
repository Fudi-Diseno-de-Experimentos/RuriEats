package com.api.rurieats.restaurants.application.internal.commandservices;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import com.api.rurieats.restaurants.domain.model.commands.CreateDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateDishCommand;
import com.api.rurieats.restaurants.domain.services.DishCommandService;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.DishRepository;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.RestaurantRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.api.rurieats.shared.domain.model.events.DishCreatedEvent;

import java.util.Optional;

@Service
public class DishCommandServiceImpl implements DishCommandService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DishCommandServiceImpl(DishRepository dishRepository, RestaurantRepository restaurantRepository, ApplicationEventPublisher eventPublisher) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<Dish> handle(CreateDishCommand command) {
        return restaurantRepository.findById(command.restaurantId()).map(restaurant -> {
            Dish dish = new Dish(restaurant, command.name(), command.description(), command.price(), command.ingredients(), command.imageUrl());
            Dish savedDish = dishRepository.save(dish);
            
            if (savedDish.getImageUrl() != null && !savedDish.getImageUrl().isEmpty()) {
                eventPublisher.publishEvent(new DishCreatedEvent(
                        savedDish.getId(),
                        savedDish.getRestaurant().getId(),
                        savedDish.getRestaurant().getOwnerId().profileId(),
                        savedDish.getName(),
                        savedDish.getImageUrl()
                ));
            }
            return savedDish;
        });
    }

    @Override
    public Optional<Dish> handle(UpdateDishCommand command) {
        return dishRepository.findById(command.id()).map(dish -> {
            dish.update(command.name(), command.description(), command.price(), command.ingredients(), command.imageUrl());
            return dishRepository.save(dish);
        });
    }

    @Override
    public void handle(DeleteDishCommand command) {
        dishRepository.findById(command.id()).ifPresent(dishRepository::delete);
    }
}
