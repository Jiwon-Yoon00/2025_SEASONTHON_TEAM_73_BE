package com.season.livingmate.user.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.post.api.dto.req.PostCreateReq;
import com.season.livingmate.user.api.dto.response.ResDto;
import com.season.livingmate.user.api.dto.resquest.CreateReqDto;
import com.season.livingmate.user.api.dto.resquest.UpdateReqDto;
import com.season.livingmate.user.application.UserProfileService;
import com.season.livingmate.user.domain.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "유저프로필 생성")
    @PostMapping
    public ResponseEntity<Response<ResDto>> create(@RequestBody @Valid CreateReqDto createReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResDto resDto = userProfileService.create(createReqDto, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATE_PROFILE,resDto));
    }

    @Operation(summary = "유저프로필 수정")
    @PatchMapping
    public ResponseEntity<Response<ResDto>> update(@RequestBody @Valid UpdateReqDto updateReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ResDto resDto = userProfileService.update(updateReqDto, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.UPDATE_PROFILE,resDto));
    }

    @Operation(summary = "내 유저프로필 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<ResDto>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResDto resDto = userProfileService.getMyProfile(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_MY_PROFILE,resDto));
    }

    @Operation(summary = "상대방 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<ResDto>> getOtherProfile(@PathVariable Long userId) {
        ResDto resDto = userProfileService.getOtherProfile(userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_PROFILE, resDto));
    }
}
