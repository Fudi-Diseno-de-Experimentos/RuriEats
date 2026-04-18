package com.api.rurieats.restaurants.interfaces.rest.resources;

import java.util.UUID;

public record CreateRestaurantResource(UUID ownerId, String name, String address) {}
