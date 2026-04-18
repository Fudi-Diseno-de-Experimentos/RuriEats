package com.api.rurieats.posts.infrastructure.persistence.jpa.repositories;

import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.valueobjects.RestaurantId;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByRestaurantId(RestaurantId restaurantId);
    Page<Post> findAllByAuthorId(ProfileId authorId, Pageable pageable);
}
