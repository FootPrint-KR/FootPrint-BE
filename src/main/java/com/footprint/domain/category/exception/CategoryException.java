package com.footprint.domain.category.exception;

import com.footprint.global.exception.BusinessException;
import com.footprint.global.exception.ErrorCode;

public class CategoryException extends BusinessException {

    private CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static CategoryException notFound() {
        return new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND);
    }
}
