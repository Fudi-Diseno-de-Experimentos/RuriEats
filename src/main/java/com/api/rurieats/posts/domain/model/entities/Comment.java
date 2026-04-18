package com.api.rurieats.posts.domain.model.entities;

import com.api.rurieats.posts.domain.model.commands.AddCommentCommand;
import com.api.rurieats.shared.domain.model.entities.AuditableModel;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends AuditableModel {

    @NotNull
    @Column(name = "post_id")
    private UUID postId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "author_id"))
    @NotNull
    private ProfileId authorId;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String text;

    public Comment(AddCommentCommand command) {
        this.postId = command.postId();
        this.authorId = new ProfileId(command.authorId());
        this.text = command.text();
    }
}
