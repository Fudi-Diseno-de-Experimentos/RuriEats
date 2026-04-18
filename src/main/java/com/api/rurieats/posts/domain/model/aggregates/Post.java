package com.api.rurieats.posts.domain.model.aggregates;

import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.enums.PostType;
import com.api.rurieats.posts.domain.model.valueobjects.DishId;
import com.api.rurieats.posts.domain.model.valueobjects.RestaurantId;
import com.api.rurieats.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends AuditableAbstractAggregateRoot<Post> {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "author_id"))
    @NotNull
    private ProfileId authorId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "restaurant_id"))
    private RestaurantId restaurantId;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PostType type;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "related_dish_id"))
    private DishId relatedDishId;

    private String imageUrl;

    public Post(CreatePostCommand command) {
        this.authorId = new ProfileId(command.authorId());
        if (command.restaurantId() != null) {
            this.restaurantId = new RestaurantId(command.restaurantId());
        }
        this.content = command.content();
        this.type = command.type();
        if (command.dishId() != null) {
            this.relatedDishId = new DishId(command.dishId());
        }
        this.imageUrl = command.imageUrl();

        // Invariants
        if (this.type == PostType.NEW_DISH && this.relatedDishId == null) {
            throw new IllegalArgumentException("relatedDishId must not be null when type is NEW_DISH");
        }
    }

    public void updateContent(String newContent, String newImageUrl) {
        this.content = newContent;
        this.imageUrl = newImageUrl;
    }
}
