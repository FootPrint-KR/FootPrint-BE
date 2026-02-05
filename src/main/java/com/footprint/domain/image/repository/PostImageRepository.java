package com.footprint.domain.image.repository;

import com.footprint.domain.image.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPostIdOrderBySeqAsc(Long postId);

    int countByPostId(Long postId);

    @Query("SELECT MAX(pi.seq) FROM PostImage pi WHERE pi.post.id = :postId")
    Integer findMaxSeqByPostId(@Param("postId") Long postId);
}
