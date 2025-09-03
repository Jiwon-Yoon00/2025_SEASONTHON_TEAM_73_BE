package com.season.livingmate.map.application;

import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.map.api.dto.PostMapDetailRes;
import com.season.livingmate.map.api.dto.PostMapRes;
import com.season.livingmate.map.domain.repository.MapRepository;
import com.season.livingmate.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final MapRepository mapRepository;

    // 지도용 게시글 목록 조회 (200m 반경 랜덤 좌표)
    public Response<List<PostMapRes>> getMapPosts() {
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
}
