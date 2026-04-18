package com.api.rurieats.iam.interfaces.rest.resources;

import java.util.UUID;

public record AuthenticatedUserResource(UUID id, String username, String token) {

}
