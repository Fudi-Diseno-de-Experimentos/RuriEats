package com.api.rurieats.restaurants.domain.model.queries;

import org.springframework.data.domain.Pageable;

public record GetAllRestaurantsQuery(Pageable pageable) {
}
