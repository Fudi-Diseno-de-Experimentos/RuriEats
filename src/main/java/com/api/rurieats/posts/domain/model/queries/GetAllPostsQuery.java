package com.api.rurieats.posts.domain.model.queries;

import org.springframework.data.domain.Pageable;

public record GetAllPostsQuery(Pageable pageable) {}
