package com.api.rurieats.restaurants.interfaces.rest.resources;

import java.util.UUID;

public record RestaurantResource(UUID id, UUID ownerId, String name, String address, Double averageRating) {}
