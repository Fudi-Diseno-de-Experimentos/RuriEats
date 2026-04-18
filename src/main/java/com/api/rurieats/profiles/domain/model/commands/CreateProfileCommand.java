package com.api.rurieats.profiles.domain.model.commands;

import java.util.UUID;

public record CreateProfileCommand(
        UUID userId, String firstName, String lastName, String avatarUrl) {
}
