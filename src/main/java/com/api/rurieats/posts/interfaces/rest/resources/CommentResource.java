package com.api.rurieats.posts.interfaces.rest.resources;

import java.util.UUID;

public record CommentResource(UUID id, UUID postId, UUID authorId, String text) {
}
