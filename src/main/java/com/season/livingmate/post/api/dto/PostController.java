package com.season.livingmate.post.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.post.api.dto.req.PostCreateReq;
import com.season.livingmate.post.api.dto.req.PostSearchReq;
import com.season.livingmate.post.api.dto.req.PostUpdateReq;
import com.season.livingmate.post.api.dto.res.PostDetailRes;
import com.season.livingmate.post.api.dto.res.PostListRes;
import com.season.livingmate.post.application.PostService;
import com.season.livingmate.post.domain.RoomType;
import com.season.livingmate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성 API")
    @PostMapping
    public ResponseEntity<Response<Long>> createPost(@RequestBody PostCreateReq req,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Long> res = postService.createPost(req, user);
        return ResponseEntity
                .status(SuccessStatus.CREATE_POST.getStatus())
                .body(res);
    }

    @Operation(summary = "게시글 개별 조회 API")
    @GetMapping("{postId}")
    public ResponseEntity<Response<PostDetailRes>> getPostDetail(@PathVariable Long postId){
        Response<PostDetailRes> res = postService.getDetail(postId);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "게시글 목록 조회 API")
    @GetMapping
    public ResponseEntity<Response<Page<PostListRes>>> getPostList(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size){
        Response<Page<PostListRes>> res = postService.getList(PageRequest.of(page, size));
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "게시글 수정 API")
    @PatchMapping("{postId}")
    public ResponseEntity<Response<Long>> updatePost(@PathVariable Long postId,
                                                     @RequestBody PostUpdateReq req,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Long> res = postService.updatePost(postId, req, user);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("{postId}")
    public ResponseEntity<Response<Long>> deletePost(@PathVariable Long postId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Long> res = postService.deletePost(postId, user);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "게시글 검색 API")
    @PostMapping("/search")
    public ResponseEntity<Response<Page<PostListRes>>> searchPosts(
            @RequestBody PostSearchReq req,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Response<Page<PostListRes>> result = postService.searchPost(req, pageable);
        return ResponseEntity.ok(result);
    }
}
