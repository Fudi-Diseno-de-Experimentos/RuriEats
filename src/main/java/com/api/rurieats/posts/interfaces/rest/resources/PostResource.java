package com.api.rurieats.posts.interfaces.rest.resources;

import com.api.rurieats.posts.domain.model.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record PostResource(UUID id, UUID authorId, UUID restaurantId, String content, PostType type, UUID dishId, @Schema(description = "URL of the post image", example = "https://example.com/image.jpg") String imageUrl) {
}
