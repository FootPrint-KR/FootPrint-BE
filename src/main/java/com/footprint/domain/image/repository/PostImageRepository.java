package com.footprint.domain.image.repository;

import com.footprint.domain.image.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPostIdOrderBySeqAsc(Long postId);

    int countByPostId(Long postId);
}
