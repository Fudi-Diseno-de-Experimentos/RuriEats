package com.api.rurieats.restaurants.domain.model.queries;

import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;

public record GetRestaurantByOwnerIdQuery(ProfileId ownerId) {}
