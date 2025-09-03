package com.season.livingmate.map.domain.repository;

import com.season.livingmate.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

}
