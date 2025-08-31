package com.season.livingmate.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RefreshTokenResDto {

    @Schema(description = "발급된 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "발급된 Refresh Token", example = "dGhpc2lzaXJyYW5kb21zdHJpbmc...")
    private String refreshToken;

    public static RefreshTokenResDto from(String accessToken, String refreshToken) {
        return new RefreshTokenResDto(
                accessToken,
                refreshToken
        );
    }
}
