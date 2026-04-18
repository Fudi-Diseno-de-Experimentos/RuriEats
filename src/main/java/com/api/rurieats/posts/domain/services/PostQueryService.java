package com.api.rurieats.posts.domain.services;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.entities.Comment;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsByRestaurantIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostByIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostsByAuthorIdQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostQueryService {
    Optional<Post> handle(GetPostByIdQuery query);
    List<Post> handle(GetAllPostsByRestaurantIdQuery query);
    Page<Post> handle(GetAllPostsQuery query);
    Page<Post> handle(GetPostsByAuthorIdQuery query);
    List<Comment> getCommentsByPostId(UUID postId);
}
