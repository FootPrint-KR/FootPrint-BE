package com.footprint.domain.image.exception;

import com.footprint.global.exception.BusinessException;
import com.footprint.global.exception.ErrorCode;

public class ImageException extends BusinessException {

    private ImageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static ImageException notFound() {
        return new ImageException(ImageErrorCode.IMAGE_NOT_FOUND);
    }

    public static ImageException uploadFailed() {
        return new ImageException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
    }

    public static ImageException maxCountExceeded() {
        return new ImageException(ImageErrorCode.MAX_IMAGE_COUNT_EXCEEDED);
    }

    public static ImageException invalidFileType() {
        return new ImageException(ImageErrorCode.INVALID_FILE_TYPE);
    }

    public static ImageException unauthorized() {
        return new ImageException(ImageErrorCode.UNAUTHORIZED);
    }

    public static ImageException invalidUrl() {
        return new ImageException(ImageErrorCode.INVALID_URL);
    }
}
