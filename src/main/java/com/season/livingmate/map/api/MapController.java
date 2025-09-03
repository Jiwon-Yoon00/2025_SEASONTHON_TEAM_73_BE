package com.season.livingmate.map.api;

import com.season.livingmate.exception.Response;
import com.season.livingmate.post.api.dto.res.PostMapDetailRes;
import com.season.livingmate.post.api.dto.res.PostMapRes;
import com.season.livingmate.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Map API", description = "지도 관련 API")
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final PostService postService;

    @Operation(summary = "지도 핀(마커) 조회 API", description = "지도에 표시할 게시글 핀들을 조회합니다. 실제 좌표에서 200m 반경 랜덤 좌표로 표시됩니다.")
    @GetMapping("/posts")
    public ResponseEntity<Response<List<PostMapRes>>> getMapPosts(
            @RequestParam(required = false) String region) {
        Response<List<PostMapRes>> res = (region != null && !region.isBlank()) 
                ? postService.getMapPostsByRegion(region)
                : postService.getMapPosts();
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "지도 핀 클릭 시 상세 정보 조회 API", description = "지도에서 핀을 클릭했을 때 표시할 상세 정보를 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<PostMapDetailRes>> getMapMarkerDetail(@PathVariable Long postId) {
        Response<PostMapDetailRes> res = postService.getMapMarkerDetail(postId);
        return ResponseEntity.ok(res);
    }
}
