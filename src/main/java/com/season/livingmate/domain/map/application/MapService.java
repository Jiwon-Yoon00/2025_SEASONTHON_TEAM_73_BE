package com.season.livingmate.domain.map.application;

import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.map.api.dto.PostDetailMapRes;
import com.season.livingmate.domain.map.api.dto.PostMapDetailRes;
import com.season.livingmate.domain.map.api.dto.PostMapRes;
import com.season.livingmate.domain.map.domain.repository.MapRepository;
import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.post.domain.repository.PostSpecs;
import com.season.livingmate.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final MapRepository mapRepository;

    // 지도용 게시글 목록 조회 (200m 반경 랜덤 좌표)
    public Response<List<PostMapRes>> getMapPosts(User user) {
        // 사용자 성별로 필터링
        Specification<Post> spec = PostSpecs.matchUserGender(user.getGender());

        List<Post> posts = mapRepository.findAll();
        List<PostMapRes> mapPosts = posts.stream()
                .map(PostMapRes::from)
                .toList();
        
        return Response.success(SuccessStatus.GET_POST_LIST, mapPosts);
    }

    // 지도 마커 클릭 시 상세 정보 조회
    public Response<PostMapDetailRes> getMapMarkerDetail(Long postId) {
        Post post = mapRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));
        
        PostMapDetailRes detailRes = PostMapDetailRes.from(post);
        return Response.success(SuccessStatus.GET_POST, detailRes);
    }

    // 게시글 상세보기용 200m 반경 지도 정보 조회
    public Response<PostDetailMapRes> getDetailMap(Long postId) {
        Post post = mapRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        PostDetailMapRes mapRes = PostDetailMapRes.from(post);
        return Response.success(SuccessStatus.GET_POST, mapRes);
    }
}
