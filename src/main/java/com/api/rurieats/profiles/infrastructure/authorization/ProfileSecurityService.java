package com.api.rurieats.profiles.infrastructure.authorization;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.profiles.domain.model.queries.GetProfileByIdQuery;
import com.api.rurieats.profiles.domain.services.ProfileQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for profile security checks
 * <p>
 *     This service provides methods to check if a user has access to a profile.
 * </p>
 */
@Service("profileSecurity")
public class ProfileSecurityService {

    private final ProfileQueryService profileQueryService;

    /**
     * Constructor
     * @param profileQueryService The {@link ProfileQueryService} instance
     */
    public ProfileSecurityService(ProfileQueryService profileQueryService) {
        this.profileQueryService = profileQueryService;
    }

    /**
     * Check if the authenticated user is the owner of the profile
     * @param authentication The authentication object
     * @param profileId The profile ID to check
     * @return true if the user is the owner of the profile, false otherwise
     */
    public boolean isOwner(Authentication authentication, UUID profileId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Get the authenticated user ID
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UUID authenticatedUserId = userDetails.getUserId();

        // Find the profile and verify it belongs to the user
        var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));

        return profile.isPresent() &&
                profile.get().getUserId().userId().equals(authenticatedUserId);
    }
}
