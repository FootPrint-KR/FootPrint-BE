package com.footprint.domain.image.dto;

import com.footprint.domain.image.entity.PostImage;

public record ImageResponse(
        Long id,
        Long postId,
        String url,
        Integer seq
) {
    public static ImageResponse from(PostImage image) {
        return new ImageResponse(
                image.getId(),
                image.getPost().getId(),
                image.getUrl(),
                image.getSeq()
        );
    }
}
