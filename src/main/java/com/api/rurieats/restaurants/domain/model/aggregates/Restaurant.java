package com.api.rurieats.restaurants.domain.model.aggregates;

import com.api.rurieats.restaurants.domain.model.entities.Dish;
import com.api.rurieats.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Restaurant extends AuditableAbstractAggregateRoot<Restaurant> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "profileId", column = @Column(name = "owner_id", nullable = false))
    })
    private ProfileId ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private Double averageRating = 0.0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dish> dishes = new ArrayList<>();

    public Restaurant() {
        // Default constructor for JPA
    }

    public Restaurant(ProfileId ownerId, String name, String address) {
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
    }

    public void update(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }
}
