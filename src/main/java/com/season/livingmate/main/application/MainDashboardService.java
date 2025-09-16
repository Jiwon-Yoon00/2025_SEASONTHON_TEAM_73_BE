package com.season.livingmate.main.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.gpt.api.dto.response.UserRecommendationResDto;
import com.season.livingmate.gpt.application.UserRecommendationService;
import com.season.livingmate.main.api.dto.DashboardResDto;
import com.season.livingmate.user.api.dto.response.LikeGetResDto;
import com.season.livingmate.user.application.UserProfileLikeService;
import com.season.livingmate.user.domain.User;
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

    public DashboardResDto getNoRoomDashboard(User user) {
        // 찜한 사용자 조회
        Pageable pageable = PageRequest.of(0, 5);
        Page<LikeGetResDto> likedUsersPage = userProfileLikeService.getLike(
                new CustomUserDetails(user),
                pageable
        );

        List<DashboardResDto.LikedUserCard> likedUsers = likedUsersPage.getContent().stream()
                .map(this::convertToLikedUserCard)
                .collect(Collectors.toList());

        // 성향조사 완료 여부 확인
        boolean isSurveyCompleted = user.isPersonalitySurveyCompleted();

        if (isSurveyCompleted) {
            // 성향조사 완료: 추천 사용자 조회
            List<DashboardResDto.RecommendedUserCard> recommendedUsers = getRecommendedUsers(user);

            return DashboardResDto.builder()
                    .recommendedUsers(new DashboardResDto.RecommendedUsers(recommendedUsers))
                    .likedUsers(new DashboardResDto.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(true)
                    .build();
        } else {
            // 성향조사 미완료: 추천 사용자 없음
            return DashboardResDto.builder()
                    .recommendedUsers(new DashboardResDto.RecommendedUsers(List.of()))
                    .likedUsers(new DashboardResDto.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(false)
                    .build();
        }
    }

    public DashboardResDto getHasRoomDashboard(User user) {
        // 찜한 사용자 조회
        Pageable pageable = PageRequest.of(0, 5);
        Page<LikeGetResDto> likedUsersPage = userProfileLikeService.getLike(
                new CustomUserDetails(user),
                pageable
        );

        List<DashboardResDto.LikedUserCard> likedUsers = likedUsersPage.getContent().stream()
                .map(this::convertToLikedUserCard)
                .collect(Collectors.toList());

        // 성향조사 완료 여부 확인
        boolean isSurveyCompleted = user.isPersonalitySurveyCompleted();

        if (isSurveyCompleted) {
            // 성향조사 완료: 추천 사용자 조회
            List<DashboardResDto.RecommendedUserCard> recommendedUsers = getRecommendedUsers(user);

            return DashboardResDto.builder()
                    .recommendedUsers(new DashboardResDto.RecommendedUsers(recommendedUsers))
                    .likedUsers(new DashboardResDto.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(true)
                    .build();
        } else {
            // 성향조사 미완료: 추천 사용자 없음
            return DashboardResDto.builder()
                    .recommendedUsers(new DashboardResDto.RecommendedUsers(List.of()))
                    .likedUsers(new DashboardResDto.LikedUsers(likedUsers))
                    .isPersonalitySurveyCompleted(false)
                    .build();
        }
    }

    private List<DashboardResDto.RecommendedUserCard> getRecommendedUsers(User user) {
        try{
            // 유저 추천 호출
            List<UserRecommendationResDto> recommendations = userRecommendationService.recommendUsers(user);

            return recommendations.stream()
                    .limit(10)
                    .map(this::convertToRecommendedUserCard)
                    .collect(Collectors.toList());
        } catch (Exception e){
            // 추천 실패하면 빈 리스트
            return List.of();
        }
    }

    private DashboardResDto.LikedUserCard convertToLikedUserCard(LikeGetResDto likeGetResDto) {
        return new DashboardResDto.LikedUserCard(
                likeGetResDto.getLikedUserId(),
                likeGetResDto.getNickname(),
                likeGetResDto.getGender().getDescription(),
                likeGetResDto.getAge(),
                likeGetResDto.isSmoking()
        );
    }
        private DashboardResDto.RecommendedUserCard convertToRecommendedUserCard(UserRecommendationResDto recommendation){
            return new DashboardResDto.RecommendedUserCard(
                    recommendation.user().id(),
                    recommendation.user().nickname(),
                    recommendation.user().profileImageUrl()
            );
        }
}
