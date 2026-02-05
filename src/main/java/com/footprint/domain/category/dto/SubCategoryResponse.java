package com.footprint.domain.category.dto;

import com.footprint.domain.category.entity.SubCategory;

public record SubCategoryResponse(
        Long id,
        String name
) {
    public static SubCategoryResponse from(SubCategory subCategory) {
        return new SubCategoryResponse(subCategory.getId(), subCategory.getName());
    }
}
