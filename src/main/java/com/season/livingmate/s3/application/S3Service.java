package com.season.livingmate.s3.application;

import com.season.livingmate.s3.S3Folder;
import com.season.livingmate.s3.api.dto.S3UploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Manager s3Manager;

    // 게시글 이미지 업로드
    public S3UploadResult uploadPostImage(MultipartFile file, Long postId) {
        return s3Manager.upload(file, S3Folder.POST, postId);
    }

    // 게시글 여러 이미지 업로드
    public List<S3UploadResult> uploadPostImages(List<MultipartFile> files, Long postId) {
        return s3Manager.uploadMultiple(files, S3Folder.POST, postId);
    }

    // 채팅 이미지 업로드
    public S3UploadResult uploadChatImage(MultipartFile file, Long ChatRoomId) {
        return s3Manager.uploadChatFile(file, ChatRoomId);
    }

    // 프로필 이미지 업로드
    public S3UploadResult uploadProfile(MultipartFile file, Long userId) {
        return s3Manager.uploadProfileFile(file, userId);
    }

    // 인증서 파일 업로드
    public S3UploadResult uploadCertificate(MultipartFile file, Long userId){
        return s3Manager.uploadCertificate(file, userId);
    }

    // 파일 삭제
    public void deleteFile(String key){
        s3Manager.delete(key);
    }
}
