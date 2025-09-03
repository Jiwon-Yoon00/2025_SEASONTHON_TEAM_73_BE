package com.season.livingmate.post.domain.repository;

import com.season.livingmate.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    
    // 지역 라벨에 특정 문자열이 포함된 게시글 검색
    List<Post> findByRegionLabelContaining(String region);
}
