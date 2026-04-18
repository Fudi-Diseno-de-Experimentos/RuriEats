package com.api.rurieats.restaurants.interfaces.rest.resources;

public record UpdateDishResource(String name, String description, Double priceAmount, String priceCurrency, String ingredients, String imageUrl) {}
