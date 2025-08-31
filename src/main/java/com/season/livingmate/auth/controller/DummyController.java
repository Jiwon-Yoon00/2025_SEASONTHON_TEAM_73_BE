package com.season.livingmate.auth.controller;

import com.season.livingmate.auth.dto.request.LoginReqDto;
import com.season.livingmate.exception.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인/로그아웃", description = "로그인/로그아웃 API - Swagger 문서화를 위한 껍데기 컨트롤러이므로 실제작동X")
@RequestMapping("/auth")
public class DummyController {


    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 후 accessToken과 refreshToken을 반환합니다.(필터에서 실제 처리됨)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
    })
    public void swaggerLoginDummy(@RequestBody LoginReqDto request) {
        // 실제 동작은 필터에서 처리하므로 바디는 비워둬도 됨
    }

    @Operation(summary = "로그아웃",
            description = "사용자를 로그아웃 처리합니다. (필터에서 실제 처리됨)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/logout")
    public void swaggerLogoutDummy() {
        // 실제 로그아웃 로직은 LogoutFilter에서 처리됨
    }
}
