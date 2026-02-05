package com.footprint.domain.category.dto;

import com.footprint.domain.category.entity.Category;

public record CategoryResponse(
        Long id,
        String name,
        String type
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}
