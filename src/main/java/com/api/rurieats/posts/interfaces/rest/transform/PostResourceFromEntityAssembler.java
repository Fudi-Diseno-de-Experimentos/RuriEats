package com.api.rurieats.posts.interfaces.rest.transform;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.interfaces.rest.resources.PostResource;

public class PostResourceFromEntityAssembler {
    public static PostResource toResourceFromEntity(Post entity) {
        return new PostResource(
                entity.getId(),
                entity.getAuthorId().profileId(),
                entity.getRestaurantId() != null ? entity.getRestaurantId().id() : null,
                entity.getContent(),
                entity.getType(),
                entity.getRelatedDishId() != null ? entity.getRelatedDishId().id() : null,
                entity.getImageUrl()
        );
    }
}
