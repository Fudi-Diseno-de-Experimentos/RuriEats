package com.api.rurieats.profiles.application.acl;


import com.api.rurieats.profiles.domain.model.commands.*;
import com.api.rurieats.profiles.domain.model.queries.GetProfileByIdQuery;
import com.api.rurieats.profiles.domain.services.*;
import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesContextFacadeImpl(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @Override
    public UUID createProfile(UUID userId, String firstName, String lastName, String avatarUrl) {
        var createProfileCommand = new CreateProfileCommand(userId, firstName, lastName, avatarUrl);

        var profile = profileCommandService.handle(createProfileCommand);
        return profile.isEmpty() ? null : profile.get().getId();
    }

    @Override
    public boolean existsProfileById(UUID profileId) {
        var query = new GetProfileByIdQuery(profileId);
        return profileQueryService.handle(query).isPresent();
    }

    @Override
    public UUID getUserIdByProfileId(UUID profileId) {
        var query = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(query);
        if (profile.isPresent()) {
            return profile.get().getUserId().userId();
        }
        return null;
    }

    @Override
    public UUID getProfileIdByUserId(UUID userId) {
        var query = new com.api.rurieats.profiles.domain.model.queries.GetProfileByUserIdQuery(new com.api.rurieats.profiles.domain.model.valueobjects.UserId(userId));
        var profile = profileQueryService.handle(query);
        if (profile.isPresent()) {
            return profile.get().getId();
        }
        return null;
    }


}
