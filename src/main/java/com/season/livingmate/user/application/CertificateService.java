package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final UserRepository userRepository;

    private final String uploadDir = "uploads/images/certificates";

    @Transactional
    public void create(MultipartFile file, CustomUserDetails userDetails) throws IOException {
        uploadImage(file);

        // 제출 여부
        User user = userDetails.getUser();
        user.setCertified(true);
        userRepository.save(user);
    }

    private void uploadImage(MultipartFile file) {
        try {

            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어있습니다.");
            }

            // 허용할 파일 확장자 리스트
            List<String> allowedExtensions = List.of(".png", ".jpg", ".jpeg", ".gif", ".bmp", ".pdf", ".txt", ".doc", ".docx");

            String originalFilename = file.getOriginalFilename();
            String extension = "";

            // 파일 크기 검증 (5MB 제한)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("이미지 파일 크기는 5MB 이하여야 합니다.");
            }

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            if(!allowedExtensions.contains(extension.toLowerCase())) {
                throw new RuntimeException("허용되지 않는 파일 형식입니다.");
            }

            String filename = UUID.randomUUID() + extension;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }
}
