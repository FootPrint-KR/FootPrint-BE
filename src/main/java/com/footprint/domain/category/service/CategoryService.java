package com.footprint.domain.category.service;

import com.footprint.domain.category.dto.CategoryResponse;
import com.footprint.domain.category.dto.SubCategoryResponse;
import com.footprint.domain.category.exception.CategoryException;
import com.footprint.domain.category.repository.CategoryRepository;
import com.footprint.domain.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Cacheable("categories")
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Cacheable(value = "subCategories", key = "#categoryId")
    public List<SubCategoryResponse> getSubCategories(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw CategoryException.notFound();
        }
        return subCategoryRepository.findByCategoryId(categoryId).stream()
                .map(SubCategoryResponse::from)
                .toList();
    }
}
