package com.api.rurieats.restaurants.interfaces.rest.resources;

import java.util.UUID;

public record DishResource(UUID id, UUID restaurantId, String name, String description, Double priceAmount, String priceCurrency, String ingredients, String imageUrl) {}
