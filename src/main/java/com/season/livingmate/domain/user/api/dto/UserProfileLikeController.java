package com.season.livingmate.domain.user.api.dto;

import com.season.livingmate.domain.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.user.api.dto.response.UserListRes;
import com.season.livingmate.domain.user.application.UserProfileLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저프로필 좋아요", description = "유저프로필 좋아요 관련 API")
@RequestMapping("/likes")
public class UserProfileLikeController {
    private final UserProfileLikeService userProfileLikeService;


    @PostMapping("/{userId}")
    @Operation(summary = "유저프로필 좋아요 생성")
    public ResponseEntity<Response<Boolean>> createLike(@PathVariable Long userId , @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean isLiked = userProfileLikeService.createLike(userId,userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATE_LIKE, isLiked));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저프로필 좋아요 취소")
    public ResponseEntity<Response<Void>> deleteLike(@PathVariable Long userId , @AuthenticationPrincipal CustomUserDetails userDetails) {
        userProfileLikeService.deleteLike(userId, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.DELETE_LIKE, null));
    }


    @GetMapping("")
    @Operation(summary = "좋아요한 유저프로필 목록 조회 (페이지네이션)")
    public ResponseEntity<Response<Page<UserListRes>>> getLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserListRes> likedProfiles = userProfileLikeService.getLike(userDetails, pageable);

        return ResponseEntity.ok(Response.success(SuccessStatus.READ_LIKE, likedProfiles));
    }
}
