package com.api.rurieats.posts.interfaces.acl;

import com.api.rurieats.posts.domain.model.enums.PostType;
import java.util.UUID;

public interface FeedContextFacade {
    UUID createPost(UUID authorId, UUID restaurantId, String content, PostType type, UUID dishId);
}
