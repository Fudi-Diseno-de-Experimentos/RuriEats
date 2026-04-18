package com.api.rurieats.iam.interfaces.rest.transform;

import com.api.rurieats.iam.domain.model.aggregates.User;
import com.api.rurieats.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token);
    }
}
