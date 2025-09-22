package com.season.livingmate.domain.map.domain.repository;

import com.season.livingmate.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

}
