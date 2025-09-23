package com.season.livingmate.domain.userProfile.api;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.userProfile.api.dto.response.UserProfileRes;
import com.season.livingmate.domain.userProfile.api.dto.response.UserListRes;
import com.season.livingmate.domain.userProfile.api.dto.request.UserFilterReq;
import com.season.livingmate.domain.userProfile.api.dto.request.UserProfileCreateReq;
import com.season.livingmate.domain.userProfile.api.dto.request.UserProfileUpdateReq;
import com.season.livingmate.domain.userProfile.application.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @PatchMapping("/init")
    public ResponseEntity<Response<UserProfileRes>> create(@RequestBody @Valid UserProfileCreateReq userProfileCreateReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileRes userProfileRes = userProfileService.create(userProfileCreateReq, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATE_PROFILE, userProfileRes));
    }

    @Operation(summary = "유저프로필 수정")
    @PatchMapping
    public ResponseEntity<Response<UserProfileRes>> update(@RequestBody @Valid UserProfileUpdateReq userProfileUpdateReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileRes userProfileRes = userProfileService.update(userProfileUpdateReq, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.UPDATE_PROFILE, userProfileRes));
    }

    @Operation(summary = "내 유저프로필 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<UserProfileRes>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        UserProfileRes userProfileRes = userProfileService.getMyProfile(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_MY_PROFILE, userProfileRes));
    }

    @Operation(summary = "상대방 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserProfileRes>> getOtherProfile(@PathVariable Long userId) {
        UserProfileRes userProfileRes = userProfileService.getOtherProfile(userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_PROFILE, userProfileRes));
    }

    @Operation(summary = "모든 유저 프로필 조회")
    @GetMapping("/all")
    public ResponseEntity<Response<Page<UserListRes>>> getAllUserProfiles(@AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {

        Page<UserListRes> dto = userProfileService.getAllUserProfiles(userDetails, pageable);

        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }

    @Operation(summary = "필터링된 유저 프로필 조회")
    @PostMapping ("/filter")
    public ResponseEntity<Response<Page<UserListRes>>> filterUsers(
            @RequestBody UserFilterReq userFilterReq,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserListRes> dto = userProfileService.filterUsers(userFilterReq, userDetails, pageable);

        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }
}
