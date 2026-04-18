package com.api.rurieats.posts.application.internal.eventhandlers;

import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.enums.PostType;
import com.api.rurieats.posts.domain.services.PostCommandService;
import com.api.rurieats.shared.domain.model.events.DishCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DishCreatedEventHandler {

    private final PostCommandService postCommandService;

    public DishCreatedEventHandler(PostCommandService postCommandService) {
        this.postCommandService = postCommandService;
    }

    @EventListener
    public void on(DishCreatedEvent event) {
        String content = "¡Tenemos un nuevo platillo para ti! Ven a probar nuestro delicioso " + event.dishName();
        var command = new CreatePostCommand(
                event.ownerProfileId(),
                event.restaurantId(),
                content,
                PostType.NEW_DISH,
                event.dishId(),
                event.imageUrl()
        );
        postCommandService.handle(command);
    }
}