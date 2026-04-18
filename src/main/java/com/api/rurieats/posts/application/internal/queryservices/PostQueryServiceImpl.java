package com.api.rurieats.posts.application.internal.queryservices;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.entities.Comment;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsByRestaurantIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostByIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostsByAuthorIdQuery;
import com.api.rurieats.posts.domain.model.valueobjects.RestaurantId;
import com.api.rurieats.posts.domain.services.PostQueryService;
import com.api.rurieats.posts.infrastructure.persistence.jpa.repositories.CommentRepository;
import com.api.rurieats.posts.infrastructure.persistence.jpa.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostQueryServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Post> handle(GetPostByIdQuery query) {
        return postRepository.findById(query.postId());
    }

    @Override
    public List<Post> handle(GetAllPostsByRestaurantIdQuery query) {
        return postRepository.findAllByRestaurantId(new RestaurantId(query.restaurantId()));
    }

    @Override
    public Page<Post> handle(GetAllPostsQuery query) {
        return postRepository.findAll(query.pageable());
    }

    @Override
    public Page<Post> handle(GetPostsByAuthorIdQuery query) {
        return postRepository.findAllByAuthorId(query.authorId(), query.pageable());
    }

    @Override
    public List<Comment> getCommentsByPostId(UUID postId) {
        return commentRepository.findByPostId(postId);
    }
}
