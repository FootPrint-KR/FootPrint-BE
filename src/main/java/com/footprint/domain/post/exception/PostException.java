package com.footprint.domain.post.exception;

import com.footprint.global.exception.BusinessException;
import com.footprint.global.exception.ErrorCode;

public class PostException extends BusinessException {

    private PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static PostException notFound() {
        return new PostException(PostErrorCode.POST_NOT_FOUND);
    }

    public static PostException unauthorized() {
        return new PostException(PostErrorCode.UNAUTHORIZED);
    }
}
