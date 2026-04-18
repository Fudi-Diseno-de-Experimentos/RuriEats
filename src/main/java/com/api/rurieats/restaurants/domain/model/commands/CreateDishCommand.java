package com.api.rurieats.restaurants.domain.model.commands;

import com.api.rurieats.restaurants.domain.model.valueobjects.Money;

import java.util.UUID;

public record CreateDishCommand(UUID restaurantId, String name, String description, Money price, String ingredients, String imageUrl) {}
