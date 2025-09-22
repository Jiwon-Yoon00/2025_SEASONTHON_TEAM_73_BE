package com.season.livingmate.domain.s3.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.s3.S3Folder;
import com.season.livingmate.domain.s3.api.dto.S3UploadRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Manager {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 크기 제한 (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // 단일 파일 업로드 (postId 없음)
    public S3UploadRes upload(MultipartFile file, S3Folder folder) {
        // 파일 유효성 검사
        validateFile(file);

        try{
            String fileName = generateFileName(file.getOriginalFilename());
            String key = folder.getPrefix() + fileName; // 기본 폴더

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, // 버킷 이름
                    key, // s3 내 파일 경로
                    file.getInputStream(), // 파일 스트림
                    metadata
            );

            // s3에 파일 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 공개 url 생성
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            log.info("파일 업로드 성공: {}", fileUrl);

            // 업로드 결과 반환
            return new S3UploadRes(fileUrl, fileName, file.getSize(), file.getContentType());

        } catch (IOException e){
            log.error("파일 업로드 실패", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    // 단일 파일 업로드 (postId 포함)
    public S3UploadRes upload(MultipartFile file, S3Folder folder, Long postId) {
        // 파일 유효성 검사
        validateFile(file);

        try{
            String fileName = generateFileName(file.getOriginalFilename());
            String key = folder.getPrefix() + postId + "/" + fileName; // 게시글별 폴더

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, // 버킷 이름
                    key, // s3 내 파일 경로
                    file.getInputStream(), // 파일 스트림
                    metadata
            );

            // s3에 파일 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 공개 url 생성
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            log.info("파일 업로드 성공: {}", fileUrl);

            // 업로드 결과 반환
            return new S3UploadRes(fileUrl, fileName, file.getSize(), file.getContentType());

        } catch (IOException e){
            log.error("파일 업로드 실패", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    // 여러 파일 업로드 (postId 포함)
    public List<S3UploadRes> uploadMultiple(List<MultipartFile> files, S3Folder folder, Long postId) {
        List<S3UploadRes> results = new ArrayList<>();

        for(MultipartFile file : files){
            if(file != null && !file.isEmpty()){
                S3UploadRes result = upload(file, folder, postId);
                results.add(result);
            }
        }
        return results;
    }

    // 채팅방별 파일 업로드
    public S3UploadRes uploadChatFile(MultipartFile file, Long chatRoomId){
        validateFile(file);

        try{
            String fileName = generateFileName(file.getOriginalFilename());
            String key = S3Folder.CHAT.getPrefix() + chatRoomId + "/" + fileName; // 채팅방별 폴더

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, // 버킷 이름
                    key, // s3 내 파일 경로
                    file.getInputStream(), // 파일 스트림
                    metadata
            );

            // s3에 파일 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 공개 url 생성
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            log.info("파일 업로드 성공: {}", fileUrl);

            // 업로드 결과 반환
            return new S3UploadRes(fileUrl, fileName, file.getSize(), file.getContentType());

        } catch (IOException e){
            log.error("파일 업로드 실패", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    // 프로필별 파일 업로드
    public S3UploadRes uploadProfileFile(MultipartFile file, Long profileId, String existingFileUrl) {
        validateFile(file);

        try{
            // 기존 파일이 있으면 삭제
            if(existingFileUrl != null && !existingFileUrl.isEmpty()){
                String existingKey = existingFileUrl.substring(existingFileUrl.indexOf(bucket) + bucket.length() + 1);
                delete(existingKey);
            }


            String fileName = generateFileName(file.getOriginalFilename());
            String key = S3Folder.PROFILE.getPrefix() + profileId + "/" + fileName; // 프로필별 폴더

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, // 버킷 이름
                    key, // s3 내 파일 경로
                    file.getInputStream(), // 파일 스트림
                    metadata
            );

            // s3에 파일 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 공개 url 생성
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            log.info("프로필 업로드 성공: {}", fileUrl);

            // 업로드 결과 반환
            return new S3UploadRes(fileUrl, fileName, file.getSize(), file.getContentType());

        } catch (IOException e){
            log.error("프로필 업로드 실패", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "프로필 업로드에 실패했습니다.");
        }
    }

    // 인증서 파일 업로드(userId별)
    public S3UploadRes uploadCertificate(MultipartFile file, Long userId, String existingFileUrl) {
        validateFile(file);

        try{
            // 기존 파일이 있으면 삭제
            if(existingFileUrl != null && !existingFileUrl.isEmpty()){
                String existingKey = existingFileUrl.substring(existingFileUrl.indexOf(bucket) + bucket.length() + 1);
                delete(existingKey);
            }

            String fileName = generateFileName(file.getOriginalFilename());
            String key = S3Folder.CERTIFICATE.getPrefix() + userId + "/" + fileName; // 사용자별 폴더

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, // 버킷 이름
                    key, // s3 내 파일 경로
                    file.getInputStream(), // 파일 스트림
                    metadata
            );

            // s3에 파일 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 공개 url 생성
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            log.info("인증서 업로드 성공: {}", fileUrl);

            // 업로드 결과 반환
            return new S3UploadRes(fileUrl, fileName, file.getSize(), file.getContentType());

        } catch (IOException e){
            log.error("인증서 업로드 실패", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "인증서 업로드에 실패했습니다.");
        }
    }

    // s3에서 파일 삭제
    public void delete(String key){
        try{
            amazonS3Client.deleteObject(bucket, key);
            log.info("파일 삭제 성공: {}", key);
        } catch (Exception e){
            log.error("파일 삭제 실패: {}", e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다");
        }
    }

    // 파일 유효성 검사
    private void validateFile(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new CustomException(ErrorStatus.BAD_REQUEST, "파일이 비어있습니다.");
        }

        // 파일 크기 검증
        if(file.getSize() > MAX_FILE_SIZE){
            throw new CustomException(ErrorStatus.BAD_REQUEST, "파일 크기는 10MB 이하여야 합니다.");
        }
    }

    // 고유한 파일명 생성
    private String generateFileName(String originalFilename){
        String extension = "";
        if(originalFilename != null && originalFilename.contains(".")){
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
