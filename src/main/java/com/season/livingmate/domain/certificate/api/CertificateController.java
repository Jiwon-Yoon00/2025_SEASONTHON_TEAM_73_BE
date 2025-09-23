package com.season.livingmate.domain.certificate.api;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.certificate.application.CertificateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "증명서 조회", description = "내가 제출한 증명서 확인하기")
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping(value = "/upload/certificate")
    public ResponseEntity<Response<String>> upload(@AuthenticationPrincipal CustomUserDetails userDetails){
        String certificateUrl = certificateService.getCertificate(userDetails);

        if (certificateUrl == null) {
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, "제출된 증명서가 없습니다."));
        }

        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, certificateUrl));
    }
}
