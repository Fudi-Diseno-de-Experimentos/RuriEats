package com.api.rurieats.restaurants.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Money(Double amount, String currency) {
    public Money {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equal to 0");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be blank");
        }
    }
}
