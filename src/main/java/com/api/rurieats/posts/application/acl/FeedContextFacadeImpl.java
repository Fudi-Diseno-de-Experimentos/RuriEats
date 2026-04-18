package com.api.rurieats.posts.application.acl;

import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.enums.PostType;
import com.api.rurieats.posts.domain.services.PostCommandService;
import com.api.rurieats.posts.interfaces.acl.FeedContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FeedContextFacadeImpl implements FeedContextFacade {

    private final PostCommandService postCommandService;

    public FeedContextFacadeImpl(PostCommandService postCommandService) {
        this.postCommandService = postCommandService;
    }

    @Override
    public UUID createPost(UUID authorId, UUID restaurantId, String content, PostType type, UUID dishId) {
        var command = new CreatePostCommand(authorId, restaurantId, content, type, dishId, null);
        var post = postCommandService.handle(command);
        return post.map(com.api.rurieats.posts.domain.model.aggregates.Post::getId).orElse(null);
    }
}
