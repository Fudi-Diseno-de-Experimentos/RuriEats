package com.api.rurieats.iam.interfaces.rest.transform;

import com.api.rurieats.iam.domain.model.entities.Role;
import com.api.rurieats.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}
