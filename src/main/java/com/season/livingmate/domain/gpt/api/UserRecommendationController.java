package com.season.livingmate.domain.gpt.api;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.gpt.api.dto.response.UserRecommendationRes;
import com.season.livingmate.domain.gpt.application.UserRecommendationService;
import com.season.livingmate.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "AI 추천 API", description = "GPT를 이용한 사용자 추천 API")
public class UserRecommendationController {

    private final UserRecommendationService userRecommendationService;

    @Operation(summary = "사용자 추천", description = "GPT를 이용한 성향조사 기반 사용자 추천(10명)")
    @PostMapping("/recommend")
    public ResponseEntity<Response<List<UserRecommendationRes>>> recommendUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails){
        User currentUser = userDetails.getUser();
        List<UserRecommendationRes> recommendations = userRecommendationService.recommendUsers(currentUser);

        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, recommendations));
    }
}
