package com.season.livingmate.user.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.user.api.dto.response.LikeGetResDto;
import com.season.livingmate.user.application.UserProfileLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Response<Map<String, Object>>> getLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 Pageable pageable) {
        Page<LikeGetResDto> likedProfiles = userProfileLikeService.getLike(userDetails, pageable);

        Map<String, Object> responseData = Map.of(
                "content", likedProfiles.getContent(),
                "pageNumber", likedProfiles.getNumber(),
                "pageSize", likedProfiles.getSize(),
                "totalPages", likedProfiles.getTotalPages(),
                "totalElements", likedProfiles.getTotalElements(),
                "last", likedProfiles.isLast()
        );
        return ResponseEntity.ok(Response.success(SuccessStatus.READ_LIKE, responseData));
    }
}
