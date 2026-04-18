package com.api.rurieats.posts.domain.services;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.commands.DeletePostCommand;
import com.api.rurieats.posts.domain.model.commands.UpdatePostCommand;
import com.api.rurieats.posts.domain.model.commands.AddCommentCommand;
import com.api.rurieats.posts.domain.model.entities.Comment;

import java.util.Optional;
import java.util.UUID;

public interface PostCommandService {
    Optional<Post> handle(CreatePostCommand command);
    Optional<Post> handle(UpdatePostCommand command);
    void handle(DeletePostCommand command);
    Optional<Comment> handle(AddCommentCommand command);
    void deleteComment(UUID commentId);
}
