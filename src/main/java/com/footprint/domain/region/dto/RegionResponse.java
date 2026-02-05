package com.footprint.domain.region.dto;

import com.footprint.domain.region.entity.Region;

public record RegionResponse(
        Long id,
        String name
) {
    public static RegionResponse from(Region region) {
        return new RegionResponse(region.getId(), region.getName());
    }
}
