package com.api.rurieats.profiles.domain.model.commands;

import java.util.UUID;

public record UpdateProfileCommand(
        UUID profileId, String firstName, String lastName, String avatarUrl) {
}
