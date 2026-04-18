package com.api.rurieats.posts.infrastructure.authorization;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.posts.domain.model.aggregates.Post;
import com.api.rurieats.posts.domain.model.entities.Comment;
import com.api.rurieats.posts.domain.model.queries.GetPostByIdQuery;
import com.api.rurieats.posts.domain.services.PostQueryService;
import com.api.rurieats.posts.infrastructure.persistence.jpa.repositories.CommentRepository;
import com.api.rurieats.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("postSecurity")
public class PostSecurityService {

    private final PostQueryService postQueryService;
    private final CommentRepository commentRepository;
    private final ProfilesContextFacade profilesContextFacade;

    public PostSecurityService(PostQueryService postQueryService, CommentRepository commentRepository, ProfilesContextFacade profilesContextFacade) {
        this.postQueryService = postQueryService;
        this.commentRepository = commentRepository;
        this.profilesContextFacade = profilesContextFacade;
    }

    public boolean isAuthor(Authentication authentication, UUID postId) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return false;
        }

        UUID userId = userDetails.getUserId();
        UUID profileId = profilesContextFacade.getProfileIdByUserId(userId);
        
        if (profileId == null) {
            return false;
        }

        Optional<Post> postOpt = postQueryService.handle(new GetPostByIdQuery(postId));
        return postOpt.isPresent() && postOpt.get().getAuthorId().profileId().equals(profileId);
    }

    public boolean isCommentAuthor(Authentication authentication, UUID commentId) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return false;
        }

        UUID userId = userDetails.getUserId();
        UUID profileId = profilesContextFacade.getProfileIdByUserId(userId);
        
        if (profileId == null) {
            return false;
        }

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        return commentOpt.isPresent() && commentOpt.get().getAuthorId().profileId().equals(profileId);
    }
}
