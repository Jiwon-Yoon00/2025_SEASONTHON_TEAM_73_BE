package com.season.livingmate.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Folder {
    POST("post/"),
    CHAT("chat/"),
    PROFILE("profile/"),
    CERTIFICATE("certificate/");

    private final String prefix;
}
