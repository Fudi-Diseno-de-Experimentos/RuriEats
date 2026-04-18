package com.api.rurieats.restaurants.infrastructure.authorization;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import com.api.rurieats.restaurants.domain.model.aggregates.Restaurant;
import com.api.rurieats.restaurants.infrastructure.persistence.jpa.repositories.RestaurantRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("restaurantSecurity")
public class RestaurantSecurityService {

    private final RestaurantRepository restaurantRepository;
    private final ProfilesContextFacade profilesContextFacade;

    public RestaurantSecurityService(RestaurantRepository restaurantRepository, ProfilesContextFacade profilesContextFacade) {
        this.restaurantRepository = restaurantRepository;
        this.profilesContextFacade = profilesContextFacade;
    }

    public boolean isOwner(Authentication authentication, UUID restaurantId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UUID authenticatedUserId;
        try {
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();
            authenticatedUserId = userDetails.getUserId();
        } catch (Exception e) {
            return false;
        }

        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return false;
        }

        Restaurant restaurant = restaurantOpt.get();
        UUID ownerProfileId = restaurant.getOwnerId().profileId();
        
        UUID profileUserId = profilesContextFacade.getUserIdByProfileId(ownerProfileId);
        
        return authenticatedUserId.equals(profileUserId);
    }
}
