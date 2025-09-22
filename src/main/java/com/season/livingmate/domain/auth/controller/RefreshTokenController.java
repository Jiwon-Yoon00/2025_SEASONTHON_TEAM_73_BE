package com.season.livingmate.domain.auth.controller;

import com.season.livingmate.domain.auth.dto.response.RefreshTokenRes;
import com.season.livingmate.domain.auth.service.RefreshTokenService;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.global.exception.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰재발급", description = "토큰재발급 API")
@RequestMapping("/auth")
@Slf4j
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "토큰 갱신", description = "헤더의 refresh token으로 access token을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/refresh")
    public ResponseEntity<Response<RefreshTokenRes>> refreshToken(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {

        if (!isBearerToken(authHeader)) {
            return ResponseEntity.badRequest()
                    .body(Response.fail(ErrorStatus.RESOURCE_NOT_FOUND));
        }

        String refreshToken = authHeader.substring(7); // "Bearer " 제거

        try {
            RefreshTokenRes tokenResDto = refreshTokenService.refreshToken(refreshToken);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, tokenResDto));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.fail(e.getErrorCode()));
        } catch (Exception e) {
            log.error("Refresh token error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.fail(ErrorStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

}
