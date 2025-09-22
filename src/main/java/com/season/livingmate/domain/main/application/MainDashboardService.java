package com.season.livingmate.domain.main.application;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.domain.gpt.api.dto.response.UserRecommendationRes;
import com.season.livingmate.domain.gpt.application.UserRecommendationService;
import com.season.livingmate.domain.main.api.dto.DashboardRes;
import com.season.livingmate.domain.user.api.dto.response.UserListRes;
import com.season.livingmate.domain.user.application.UserProfileLikeService;
import com.season.livingmate.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainDashboardService {

    private final UserProfileLikeService userProfileLikeService;
    private final UserRecommendationService userRecommendationService;

    public DashboardRes getNoRoomDashboard(User user) {
        // 찜한 사용자 조회
        Pageable pageable = PageRequest.of(0, 5);
        Page<UserListRes> likedUsersPage = userProfileLikeService.getLike(
                new CustomUserDetails(user),
                pageable
        );

        List<DashboardRes.LikedUserCard> likedUsers = likedUsersPage.getContent().stream()
                .map(this::convertToLikedUserCard)
                .collect(Collectors.toList());

        // 성향조사 완료 여부 확인
        boolean isSurveyCompleted = user.isPersonalitySurveyCompleted();

        if (isSurveyCompleted) {
            // 성향조사 완료: 추천 사용자 조회
            List<DashboardRes.RecommendedUserCard> recommendedUsers = getRecommendedUsers(user);

            return DashboardRes.builder()
                    .recommendedUsers(new DashboardRes.RecommendedUsers(recommendedUsers))
                    .likedUsers(new DashboardRes.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(true)
                    .build();
        } else {
            // 성향조사 미완료: 추천 사용자 없음
            return DashboardRes.builder()
                    .recommendedUsers(new DashboardRes.RecommendedUsers(List.of()))
                    .likedUsers(new DashboardRes.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(false)
                    .build();
        }
    }

    public DashboardRes getHasRoomDashboard(User user) {
        // 찜한 사용자 조회
        Pageable pageable = PageRequest.of(0, 5);
        Page<UserListRes> likedUsersPage = userProfileLikeService.getLike(
                new CustomUserDetails(user),
                pageable
        );

        List<DashboardRes.LikedUserCard> likedUsers = likedUsersPage.getContent().stream()
                .map(this::convertToLikedUserCard)
                .collect(Collectors.toList());

        // 성향조사 완료 여부 확인
        boolean isSurveyCompleted = user.isPersonalitySurveyCompleted();

        if (isSurveyCompleted) {
            // 성향조사 완료: 추천 사용자 조회
            List<DashboardRes.RecommendedUserCard> recommendedUsers = getRecommendedUsers(user);

            return DashboardRes.builder()
                    .recommendedUsers(new DashboardRes.RecommendedUsers(recommendedUsers))
                    .likedUsers(new DashboardRes.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(true)
                    .build();
        } else {
            // 성향조사 미완료: 추천 사용자 없음
            return DashboardRes.builder()
                    .recommendedUsers(new DashboardRes.RecommendedUsers(List.of()))
                    .likedUsers(new DashboardRes.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(false)
                    .build();
        }
    }

    private List<DashboardRes.RecommendedUserCard> getRecommendedUsers(User user) {
        try{
            // 유저 추천 호출
            List<UserRecommendationRes> recommendations = userRecommendationService.recommendUsers(user);

            return recommendations.stream()
                    .limit(10)
                    .map(this::convertToRecommendedUserCard)
                    .collect(Collectors.toList());
        } catch (Exception e){
            // 추천 실패하면 빈 리스트
            return List.of();
        }
    }

    private DashboardRes.LikedUserCard convertToLikedUserCard(UserListRes likeGetResDto) {
        return new DashboardRes.LikedUserCard(
                likeGetResDto.getId(),
                likeGetResDto.getNickname(),
                likeGetResDto.getGender().getDescription(),
                likeGetResDto.getAge(),
                likeGetResDto.isSmoking()
        );
    }
        private DashboardRes.RecommendedUserCard convertToRecommendedUserCard(UserRecommendationRes recommendation){
            return new DashboardRes.RecommendedUserCard(
                    recommendation.user().id(),
                    recommendation.user().nickname(),
                    recommendation.user().profileImageUrl(),
                    recommendation.matchScore(),
                    recommendation.user().gender(),
                    recommendation.user().age(),
                    recommendation.user().smoking()
            );
        }
}
