package com.api.rurieats.restaurants.domain.model.entities;

import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.domain.model.valueobjects.Money;
import com.api.rurieats.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Dish extends AuditableModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency", nullable = false))
    })
    private Money price;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    private String imageUrl;

    public Dish(Restaurant restaurant, String name, String description, Money price, String ingredients, String imageUrl) {
        this.restaurant = restaurant;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String description, Money price, String ingredients, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }
}
