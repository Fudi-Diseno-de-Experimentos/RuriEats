package com.api.rurieats.profiles.application.internal.commandservices;


import com.api.rurieats.iam.interfaces.acl.IamContextFacade;
import com.api.rurieats.profiles.domain.model.aggregates.Profile;
import com.api.rurieats.profiles.domain.model.commands.*;
import com.api.rurieats.profiles.domain.model.valueobjects.UserId;
import com.api.rurieats.profiles.domain.services.ProfileCommandService;
import com.api.rurieats.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.api.rurieats.shared.domain.exceptions.DuplicateResourceException;
import com.api.rurieats.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Profile Command Service Implementation
 */
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository profileRepository;
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor
     *
     * @param profileRepository The {@link ProfileRepository} instance
     * @param iamContextFacade The {@link IamContextFacade} instance
     */
    public ProfileCommandServiceImpl(ProfileRepository profileRepository, IamContextFacade iamContextFacade) {
        this.profileRepository = profileRepository;
        this.iamContextFacade = iamContextFacade;
    }

    // inherited javadoc
    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {

        // Validate that user exists
        if (!iamContextFacade.userExists(command.userId())) {
            throw new ResourceNotFoundException("Usuario", command.userId());
        }

        // Validate that user doesn't already have a profile
        var userId = new UserId(command.userId());
        if(profileRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("un perfil", "el usuario", command.userId());
        }

        // Create new profile
        var profile = new Profile(command);

        // Save and return
        profileRepository.save(profile);
        return Optional.of(profile);


    }

    public Optional<Profile> handle(UpdateProfileCommand command) {
        // Find existing profile
        var profile = profileRepository.findById(command.profileId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", command.profileId()));

        // Update profile
        profile.updateProfile(command);

        // Save and return
        var savedProfile = profileRepository.save(profile);
        return Optional.of(savedProfile);
    }
}
