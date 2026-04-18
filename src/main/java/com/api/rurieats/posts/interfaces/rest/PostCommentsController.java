package com.api.rurieats.posts.interfaces.rest;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.posts.domain.model.commands.AddCommentCommand;
import com.api.rurieats.posts.domain.services.PostCommandService;
import com.api.rurieats.posts.domain.services.PostQueryService;
import com.api.rurieats.posts.interfaces.rest.resources.AddCommentResource;
import com.api.rurieats.posts.interfaces.rest.resources.CommentResource;
import com.api.rurieats.posts.interfaces.rest.transform.CommentResourceFromEntityAssembler;
import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/posts/{postId}/comments", produces = "application/json")
@Tag(name = "Post Comments", description = "Post Comments Management Endpoints")
public class PostCommentsController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final ProfilesContextFacade profilesContextFacade;

    public PostCommentsController(PostCommandService postCommandService, PostQueryService postQueryService, ProfilesContextFacade profilesContextFacade) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
        this.profilesContextFacade = profilesContextFacade;
    }

    @Operation(summary = "Add a comment to a post", description = "Adds a comment to a specific post. Authored by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or post does not exist"),
            @ApiResponse(responseCode = "404", description = "Profile not found for authenticated user")
    })
    @PostMapping
    public ResponseEntity<CommentResource> addComment(
            @PathVariable UUID postId,
            @RequestBody AddCommentResource resource,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID profileId = profilesContextFacade.getProfileIdByUserId(userDetails.getUserId());
        if (profileId == null) return ResponseEntity.notFound().build();

        var command = new AddCommentCommand(postId, profileId, resource.text());
        try {
            var commentOpt = postCommandService.handle(command);
            if (commentOpt.isEmpty()) return ResponseEntity.badRequest().build();

            var commentResource = CommentResourceFromEntityAssembler.toResourceFromEntity(commentOpt.get());
            return new ResponseEntity<>(commentResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get comments for a post", description = "Retrieves all comments associated with a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<CommentResource>> getComments(@PathVariable UUID postId) {
        var comments = postQueryService.getCommentsByPostId(postId);
        var commentResources = comments.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResources);
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID. Can only be done by the comment author or an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (not author)")
    })
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isCommentAuthor(authentication, #commentId)")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID postId, @PathVariable UUID commentId) {
        postCommandService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
