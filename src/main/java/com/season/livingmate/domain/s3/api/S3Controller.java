package com.season.livingmate.domain.s3.api;

import com.season.livingmate.domain.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.s3.api.dto.S3UploadRes;
import com.season.livingmate.domain.s3.application.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
@Tag(name = "S3 파일 업로드", description = "S3를 이용한 파일 업로드 API")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "게시글 이미지 업로드", description = "게시글 이미지를 S3에 업로드합니다.")
    @PostMapping(value = "/upload/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<S3UploadRes>> uploadPostImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
            S3UploadRes result = s3Service.uploadPostImage(file, postId);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, result));
    }

    @Operation(summary = "여러 게시글 이미지 업로드", description = "게시글 여러 이미지를 S3에 업로드합니다.")
    @PostMapping(value = "/upload/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<List<S3UploadRes>>> uploadPostImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
            List<S3UploadRes> results = s3Service.uploadPostImages(files, postId);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, results));
    }

    @Operation(summary = "채팅 이미지 업로드", description = "채팅방별로 이미지를 S3에 업로드합니다.")
    @PostMapping(value = "/upload/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<S3UploadRes>> uploadChatImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam("chatRoomId") Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
            S3UploadRes result = s3Service.uploadChatImage(file, chatRoomId);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, result));
    }

    @Operation(summary = "프로필 이미지 업로드", description = "프로필 이미지를 S3에 업로드합니다.")
    @PostMapping(value = "/upload/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<S3UploadRes>> uploadProfile(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Long userId = userDetails.getUser().getId(); // 인증된 사용자 ID 사용
        S3UploadRes result = s3Service.uploadProfile(file, userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, result));
    }

    @Operation(summary = "인증서 파일 업로드", description = "인증서 파일을 S3에 업로드합니다.")
    @PostMapping(value = "/upload/certificate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<S3UploadRes>> uploadCertificate(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Long userId = userDetails.getUser().getId(); // 인증된 사용자 ID 사용
        S3UploadRes result = s3Service.uploadCertificate(file, userId);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, result));
    }

    @Operation(summary = "파일 삭제", description = "S3에서 파일을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<Response<String>> deleteFile(
            @RequestParam("key") String key,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
            s3Service.deleteFile(key);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, "파일이 삭제되었습니다."));
    }
}
