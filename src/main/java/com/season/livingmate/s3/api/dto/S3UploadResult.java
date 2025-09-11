package com.season.livingmate.s3.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "S3 업로드 결과")
public class S3UploadResult {

    @Schema(description = "업로드된 파일의 S3 URL", example = "https://bucket.s3.region.amazonaws.com/post/uuid.jpg")
    private String fileUrl;

    @Schema(description = "생성된 파일명", example = "uuid.jpg")
    private String fileName;

    @Schema(description = "파일 크기 (bytes)", example = "1024000")
    private Long fileSize;

    @Schema(description = "파일 타입", example = "image/jpeg")
    private String contentType;
}
