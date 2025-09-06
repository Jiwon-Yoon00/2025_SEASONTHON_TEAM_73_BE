package com.season.livingmate.post.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.post.api.dto.req.PostSearchReq;
import com.season.livingmate.post.api.dto.res.PostDetailRes;
import com.season.livingmate.post.api.dto.res.PostListRes;

import com.season.livingmate.post.application.PostService;
import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Long>> createPost(@RequestPart(value = "data", required = true) String dataJson,
                                                     @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Long> res = postService.createPost(dataJson, imageFiles, user);
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
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Page<PostListRes>> res = postService.getList(PageRequest.of(page, size), user);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "게시글 수정 API")
    @PatchMapping(value = "{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Long>> updatePost(@PathVariable Long postId,
                                                     @RequestPart(value = "data", required = true) String dataJson,
                                                     @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Response<Long> res = postService.updatePost(postId, dataJson, imageFiles, user);
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
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, size);
        Response<Page<PostListRes>> result = postService.searchPost(req, pageable, user);
        return ResponseEntity.ok(result);
    }
}
