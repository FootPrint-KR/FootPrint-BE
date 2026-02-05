package com.footprint.domain.post.dto;

import com.footprint.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String summary,
        String authorNickname,
        String cityName,
        String subCategoryName,
        Integer viewCount,
        Integer likeCount,
        String status,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getUser().getNickname(),
                post.getCity().getName(),
                post.getSubCategory().getName(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getStatus(),
                post.getCreatedAt()
        );
    }
}
