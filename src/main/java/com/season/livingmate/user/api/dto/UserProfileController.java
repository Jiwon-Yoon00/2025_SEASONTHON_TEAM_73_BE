package com.season.livingmate.user.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.user.api.dto.response.UserProfileResDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileCreateReqDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileUpdateReqDto;
import com.season.livingmate.user.application.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Validated
@Tag(name = "유저프로필")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "유저프로필 생성")
    @PostMapping
    public ResponseEntity<Response<UserProfileResDto>> create(@RequestBody @Valid UserProfileCreateReqDto userProfileCreateReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResDto userProfileResDto = userProfileService.create(userProfileCreateReqDto, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATE_PROFILE, userProfileResDto));
    }

    @Operation(summary = "유저프로필 수정")
    @PatchMapping
    public ResponseEntity<Response<UserProfileResDto>> update(@RequestBody @Valid UserProfileUpdateReqDto userProfileUpdateReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResDto userProfileResDto = userProfileService.update(userProfileUpdateReqDto, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.UPDATE_PROFILE, userProfileResDto));
    }

    @Operation(summary = "내 유저프로필 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<UserProfileResDto>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        UserProfileResDto userProfileResDto = userProfileService.getMyProfile(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_MY_PROFILE, userProfileResDto));
    }

    @Operation(summary = "상대방 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserProfileResDto>> getOtherProfile(@PathVariable Long userId) {
        UserProfileResDto userProfileResDto = userProfileService.getOtherProfile(userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_PROFILE, userProfileResDto));
    }
}
