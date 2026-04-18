package com.api.rurieats.posts.application.internal.commandservices;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.commands.AddCommentCommand;
import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.commands.DeletePostCommand;
import com.api.rurieats.posts.domain.model.commands.UpdatePostCommand;
import com.api.rurieats.posts.domain.model.entities.Comment;
import com.api.rurieats.posts.domain.services.PostCommandService;
import com.api.rurieats.posts.infrastructure.persistence.jpa.repositories.CommentRepository;
import com.api.rurieats.posts.infrastructure.persistence.jpa.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostCommandServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Post> handle(CreatePostCommand command) {
        var post = new Post(command);
        var createdPost = postRepository.save(post);
        return Optional.of(createdPost);
    }

    @Override
    public Optional<Post> handle(UpdatePostCommand command) {
        return postRepository.findById(command.postId()).map(post -> {
            post.updateContent(command.content(), command.imageUrl());
            return postRepository.save(post);
        });
    }

    @Override
    public void handle(DeletePostCommand command) {
        postRepository.findById(command.postId()).ifPresent(postRepository::delete);
    }

    @Override
    public Optional<Comment> handle(AddCommentCommand command) {
        if (!postRepository.existsById(command.postId())) {
            throw new IllegalArgumentException("Post does not exist");
        }
        var comment = new Comment(command);
        var createdComment = commentRepository.save(comment);
        return Optional.of(createdComment);
    }

    @Override
    public void deleteComment(UUID commentId) {
        commentRepository.findById(commentId).ifPresent(commentRepository::delete);
    }
}
