package com.api.rurieats.posts.domain.model.commands;

import com.api.rurieats.posts.domain.model.enums.PostType;
import java.util.UUID;

public record CreatePostCommand(UUID authorId, UUID restaurantId, String content, PostType type, UUID dishId, String imageUrl) {
}
