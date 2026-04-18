package com.api.rurieats.profiles.domain.model.aggregates;

import com.api.rurieats.profiles.domain.model.commands.CreateProfileCommand;
import com.api.rurieats.profiles.domain.model.commands.UpdateProfileCommand;
import com.api.rurieats.profiles.domain.model.valueobjects.UserId;
import com.api.rurieats.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    @Embedded
    @NotNull
    private UserId userId;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Size(max = 255)
    private String avatarUrl;

    public Profile() {
        // Default constructor for JPA
    }
    public Profile(CreateProfileCommand command) {
        this.userId = new UserId(command.userId());
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.avatarUrl = command.avatarUrl();
    }

    public Profile updateProfile(UpdateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.avatarUrl = command.avatarUrl();
        return this;
    }

}