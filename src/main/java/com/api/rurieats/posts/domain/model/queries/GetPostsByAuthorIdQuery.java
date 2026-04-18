package com.api.rurieats.posts.domain.model.queries;

import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;
import org.springframework.data.domain.Pageable;

public record GetPostsByAuthorIdQuery(ProfileId authorId, Pageable pageable) {}
