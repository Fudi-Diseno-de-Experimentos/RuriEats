package com.api.rurieats.posts.interfaces.rest.transform;

import com.api.rurieats.posts.domain.model.entities.Comment;
import com.api.rurieats.posts.interfaces.rest.resources.CommentResource;

public class CommentResourceFromEntityAssembler {
    public static CommentResource toResourceFromEntity(Comment entity) {
        return new CommentResource(
                entity.getId(),
                entity.getPostId(),
                entity.getAuthorId().profileId(),
                entity.getText()
        );
    }
}
