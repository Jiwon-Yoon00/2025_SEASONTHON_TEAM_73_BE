package com.season.livingmate.user.api.dto;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.user.api.dto.response.UserProfileResDto;
import com.season.livingmate.user.api.dto.response.UserResDto;
import com.season.livingmate.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저정보")
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저정보 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<UserResDto>> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResDto dto =  userService.getUser(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }

    @Operation(summary = "상대방 유저 정보 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserResDto>> getOther(@PathVariable Long userId) {
        UserResDto dto = userService.getOther(userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }
}
