package com.footprint.domain.region.service;

import com.footprint.domain.region.dto.CityResponse;
import com.footprint.domain.region.dto.RegionResponse;
import com.footprint.domain.region.repository.CityRepository;
import com.footprint.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;

    @Cacheable("regions")
    public List<RegionResponse> getRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponse::from)
                .toList();
    }

    @Cacheable(value = "cities", key = "#regionId")
    public List<CityResponse> getCities(Long regionId) {
        return cityRepository.findByRegionId(regionId).stream()
                .map(CityResponse::from)
                .toList();
    }
}
