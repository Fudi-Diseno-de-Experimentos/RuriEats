package com.api.rurieats.posts.domain.model.commands;

import java.util.UUID;

public record UpdatePostCommand(UUID postId, String content, String imageUrl) {
}
