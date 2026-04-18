package com.api.rurieats.restaurants.domain.model.commands;

import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;

public record CreateRestaurantCommand(ProfileId ownerId, String name, String address) {}
