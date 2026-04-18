package com.api.rurieats.posts.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePostResource(String content, @Schema(description = "URL of the post image", example = "https://example.com/image.jpg") String imageUrl) {
}
