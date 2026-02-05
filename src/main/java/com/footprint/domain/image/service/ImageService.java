package com.footprint.domain.image.service;

import com.footprint.domain.image.dto.ImageResponse;
import com.footprint.domain.image.entity.PostImage;
import com.footprint.domain.image.exception.ImageException;
import com.footprint.domain.image.repository.PostImageRepository;
import com.footprint.domain.post.entity.Post;
import com.footprint.domain.post.exception.PostException;
import com.footprint.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private static final int MAX_IMAGE_COUNT = 10;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public ImageResponse uploadImage(Long userId, Long postId, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostException::notFound);

        if (!post.isOwner(userId)) {
            throw PostException.unauthorized();
        }

        int currentCount = postImageRepository.countByPostId(postId);
        if (currentCount >= MAX_IMAGE_COUNT) {
            throw ImageException.maxCountExceeded();
        }

        validateFileType(file);

        String url = uploadToS3(file, postId);
        int seq = currentCount + 1;

        PostImage image = PostImage.builder()
                .post(post)
                .url(url)
                .seq(seq)
                .build();
        postImageRepository.save(image);

        return ImageResponse.from(image);
    }

    @Transactional
    public void deleteImage(Long userId, Long imageId) {
        PostImage image = postImageRepository.findById(imageId)
                .orElseThrow(ImageException::notFound);

        if (!image.getPost().isOwner(userId)) {
            throw ImageException.unauthorized();
        }

        deleteFromS3(image.getUrl());
        postImageRepository.delete(image);
    }

    public List<ImageResponse> getImages(Long postId) {
        return postImageRepository.findByPostIdOrderBySeqAsc(postId).stream()
                .map(ImageResponse::from)
                .toList();
    }

    private void validateFileType(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw ImageException.invalidFileType();
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw ImageException.invalidFileType();
        }
    }

    private String uploadToS3(MultipartFile file, Long postId) {
        try {
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf("."));
            String key = "posts/" + postId + "/" + UUID.randomUUID() + extension;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        } catch (IOException e) {
            throw ImageException.uploadFailed();
        }
    }

    private void deleteFromS3(String url) {
        String key = url.substring(url.lastIndexOf(".com/") + 5);

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
}
