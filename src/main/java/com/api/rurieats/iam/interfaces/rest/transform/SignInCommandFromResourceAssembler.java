package com.api.rurieats.iam.interfaces.rest.transform;

import com.api.rurieats.iam.domain.model.commands.SignInCommand;
import com.api.rurieats.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}
