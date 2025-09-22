package com.season.livingmate.domain.user.api;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.user.api.dto.response.UserListRes;
import com.season.livingmate.domain.user.application.UserService;
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
    public ResponseEntity<Response<UserListRes>> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserListRes dto =  userService.getUser(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }

    @Operation(summary = "상대방 유저 정보 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserListRes>> getOther(@PathVariable Long userId) {
        UserListRes dto = userService.getOther(userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }
}
