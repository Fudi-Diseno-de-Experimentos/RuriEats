package com.api.rurieats.posts.interfaces.rest;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.posts.domain.model.commands.CreatePostCommand;
import com.api.rurieats.posts.domain.model.commands.DeletePostCommand;
import com.api.rurieats.posts.domain.model.commands.UpdatePostCommand;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsByRestaurantIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetAllPostsQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostByIdQuery;
import com.api.rurieats.posts.domain.model.queries.GetPostsByAuthorIdQuery;
import com.api.rurieats.posts.domain.services.PostCommandService;
import com.api.rurieats.posts.domain.services.PostQueryService;
import com.api.rurieats.posts.interfaces.rest.resources.CreatePostResource;
import com.api.rurieats.posts.interfaces.rest.resources.PostResource;
import com.api.rurieats.posts.interfaces.rest.resources.UpdatePostResource;
import com.api.rurieats.posts.interfaces.rest.transform.PostResourceFromEntityAssembler;
import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/posts", produces = "application/json")
@Tag(name = "Posts", description = "Posts Management Endpoints")
public class PostsController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final ProfilesContextFacade profilesContextFacade;

    public PostsController(PostCommandService postCommandService, PostQueryService postQueryService, ProfilesContextFacade profilesContextFacade) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
        this.profilesContextFacade = profilesContextFacade;
    }

    @Operation(summary = "Get all posts", description = "Retrieves all posts with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<PostResource>> getAllPosts(
            @Parameter(description = "Pagination information", required = false)
            Pageable pageable) {
        var query = new GetAllPostsQuery(pageable);
        var posts = postQueryService.handle(query);
        var resources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get posts by author profile ID", description = "Retrieves posts by a specific author profile ID with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<Page<PostResource>> getPostsByAuthorId(
            @PathVariable UUID profileId,
            @Parameter(description = "Pagination information", required = false)
            Pageable pageable) {
        var query = new GetPostsByAuthorIdQuery(new ProfileId(profileId), pageable);
        var posts = postQueryService.handle(query);
        var resources = posts.map(PostResourceFromEntityAssembler::toResourceFromEntity);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Create a new post", description = "Creates a new post authored by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Profile not found for authenticated user")
    })
    @PostMapping
    public ResponseEntity<PostResource> createPost(
            @RequestBody CreatePostResource resource,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID profileId = profilesContextFacade.getProfileIdByUserId(userDetails.getUserId());
        if (profileId == null) return ResponseEntity.notFound().build();

        var command = new CreatePostCommand(
                profileId,
                resource.restaurantId(),
                resource.content(),
                resource.type(),
                resource.dishId(),
                resource.imageUrl()
        );

        var post = postCommandService.handle(command);
        if (post.isEmpty()) return ResponseEntity.badRequest().build();

        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(post.get());
        return new ResponseEntity<>(postResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a post by ID", description = "Retrieves a post's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostResource> getPostById(@PathVariable UUID postId) {
        var getPostByIdQuery = new GetPostByIdQuery(postId);
        var post = postQueryService.handle(getPostByIdQuery);
        if (post.isEmpty()) return ResponseEntity.notFound().build();

        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(post.get());
        return ResponseEntity.ok(postResource);
    }

    @Operation(summary = "Get all posts by restaurant", description = "Retrieves all posts related to a specific restaurant ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<PostResource>> getAllPostsByRestaurantId(@PathVariable UUID restaurantId) {
        var query = new GetAllPostsByRestaurantIdQuery(restaurantId);
        var posts = postQueryService.handle(query);
        var postResources = posts.stream()
                .map(PostResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResources);
    }

    @Operation(summary = "Update a post", description = "Updates a post's content. Can only be done by the author or an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (not author)"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isAuthor(authentication, #postId)")
    public ResponseEntity<PostResource> updatePost(@PathVariable UUID postId, @RequestBody UpdatePostResource resource) {
        var command = new UpdatePostCommand(postId, resource.content(), resource.imageUrl());
        var updatedPost = postCommandService.handle(command);
        if (updatedPost.isEmpty()) return ResponseEntity.notFound().build();

        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(updatedPost.get());
        return ResponseEntity.ok(postResource);
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by its ID. Can only be done by the author or an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (not author)")
    })
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isAuthor(authentication, #postId)")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postCommandService.handle(new DeletePostCommand(postId));
        return ResponseEntity.noContent().build();
    }
}
