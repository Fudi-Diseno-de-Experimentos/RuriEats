package com.api.rurieats.posts.domain.model.commands;

import java.util.UUID;

public record AddCommentCommand(UUID postId, UUID authorId, String text) {
}
