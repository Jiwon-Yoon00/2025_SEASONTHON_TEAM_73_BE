package com.season.livingmate.gpt.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.gpt.api.dto.response.UserRecommendationResDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserRecommendationService {

    private final UserRepository userRepository;
    private final GptService gptService;
    private final ObjectMapper objectMapper;

    public List<UserRecommendationResDto> recommendUsers(User currentUser) {
        // 현재 사용자 프로필(성향조사) 확인
        UserProfile currentProfile = currentUser.getUserProfile();
        if(currentProfile == null) {
            throw new CustomException(ErrorStatus.USER_SURVEY_NOT_COMPLETED);
        }

        // 사용자 프로필에서 가중치 가져오기
        List<String> selectedItems = currentProfile.getRecommendationWeights();
        if(selectedItems == null || selectedItems.isEmpty()) {
            throw new CustomException(ErrorStatus.RECOMMENDATION_WEIGHTS_NOT_SET);
        }

        // 추천 대상자 필터링 (isRoom 으로)
        List<User> candidates = getCandidates(currentUser);

        // 가중치 검증
        validateSelectedItems(selectedItems);

        // 선택된 3개는 높은 가중치 나머지 9개는 낮은 가중치
        Map<String, Integer> weights = calculateWeights(selectedItems);

        // 추천 (10명만)
        return recommendWithGpt(currentProfile, candidates, weights, selectedItems);
    }

    private List<UserRecommendationResDto> recommendWithGpt(UserProfile currentProfile, List<User> candidates, Map<String, Integer> weights, List<String> selectedItems) {
        try {
            // 현재 사용자 프로필 텍스트로 변환
            String currentUserProfile = buildUserProfileText(currentProfile);

            // 후보자 프로필 텍스트 변환
            List<String> candidateProfiles = candidates.stream()
                    .map(user -> String.format("사용자 ID: %d\n닉네임: %s\n%s",
                            user.getId(),
                            user.getNickname(),
                            buildUserProfileText(user.getUserProfile())))
                    .collect(Collectors.toList());

            // 가중치 텍스트 변환
            String weightsText = buildWeightsText(weights);

            // gpt api 호출
            String gptResponse = gptService.recommendUsers(currentUserProfile, candidateProfiles, weightsText, selectedItems);

            // gpt 응답으로 추천 결과 생성
            return parseGptResponse(gptResponse, candidates);

        }catch (Exception e){
            throw new CustomException(ErrorStatus.RECOMMENDATION_SERVICE_ERROR);
        }
    }

    private Map<String, Integer> calculateWeights(List<String> selectedItems) {
        Map<String, Integer> weights = new HashMap<>();

        // 무조건 3개 선택 각 25%씩
        for (String item : selectedItems) {
            weights.put(item, 25);
        }
        int remainingWeight = 3;

        String[] allItems = {
                "smellLevel", "sleepLevel", "tidinessLevel", "bathroomCleaningLevel",
                "cookingCount", "alcoholCount", "dishShare", "phoneMode",
                "earphoneUsage", "smoking", "sleepHabit", "pet"
        };

        for (String item : allItems) {
            if (!selectedItems.contains(item)) {
                weights.put(item, remainingWeight);
            }
        }
        return weights;
    }

    private void validateSelectedItems(List<String> selectedItems) {
        // 3개 선택했는지 확인
        if (selectedItems == null || selectedItems.size() != 3) {
            throw new CustomException(ErrorStatus.INVALID_SELECTED_ITEMS_COUNT);
        }

        Set<String> validItems = Set.of(
                "smellLevel", "sleepLevel", "tidinessLevel", "bathroomCleaningLevel",
                "cookingCount", "alcoholCount", "dishShare", "phoneMode",
                "earphoneUsage", "smoking", "sleepHabit", "pet"
        );

        for (String item : selectedItems) {
            if (!validItems.contains(item)) {
                throw new CustomException(ErrorStatus.INVALID_SELECTED_ITEMS_FIELD);
            }
        }

        // 중복 체크
        if (selectedItems.size() != selectedItems.stream().distinct().count()) {
            throw new CustomException(ErrorStatus.DUPLICATE_SELECTED_ITEMS);
        }
    }

    private String buildUserProfileText(UserProfile profile) {
        if (profile == null) return "프로필 없음";

        return String.format("""
                냄새 민감도: %s
                잠귀 민감도: %s
                정리정돈 성향: %s
                화장실 청소 빈도: %s
                요리 빈도: %s
                음주 횟수: %s
                식기류 공유: %s
                휴대폰 모드: %s
                이어폰 사용: %s
                흡연 여부: %s
                잠버릇: %s
                반려동물: %s
                """,
                profile.getSmellLevel(),
                profile.getSleepLevel(),
                profile.getTidinessLevel(),
                profile.getBathroomCleaningLevel(),
                profile.getCookingCount(),
                profile.getAlcoholCount(),
                profile.getDishShare(),
                profile.getPhoneMode(),
                profile.getEarphoneUsage(),
                profile.isSmoking() ? "흡연" : "비흡연",
                formatSleepHabit(profile.getSleepHabit()),
                formatPet(profile.getPet())
        );
    }

    private String formatSleepHabit(List<String> sleepHabits) {
        if (sleepHabits == null || sleepHabits.isEmpty()) {
            return "없음";
        }

        List<String> koreanHabits = sleepHabits.stream()
                .map(this::convertSleepHabitToKorean)
                .collect(Collectors.toList());

        return String.join(", ", koreanHabits);
    }

    private String convertSleepHabitToKorean(String habit) {
        return switch (habit) {
            case "SNORE" -> "코골이";
            case "TEETH_GRINDING" -> "이갈이";
            case "NONE" -> "없음";
            default -> habit; // 변환할 수 없으면 원본 반환
        };
    }

    private String formatPet(List<String> pets) {
        if (pets == null || pets.isEmpty()) {
            return "무";
        }

        List<String> koreanPets = pets.stream()
                .map(this::convertPetToKorean)
                .collect(Collectors.toList());

        return "유(" + String.join(", ", koreanPets) + ")";
    }

    private String convertPetToKorean(String pet) {
        return switch (pet) {
            case "DOG" -> "강아지";
            case "CAT" -> "고양이";
            case "FISH" -> "물고기";
            case "AMPHIBIAN" -> "양서류";
            case "REPTILE" -> "파충류";
            case "INVERTEBRATE" -> "무척추동물(곤충)";
            case "BIRD" -> "조류";
            default -> pet; // 변환할 수 없으면 원본 반환
        };
    }

    private String buildWeightsText(Map<String, Integer> weights) {
        StringBuilder sb = new StringBuilder();
        sb.append("우선순위 가중치:\n");
        for (Map.Entry<String, Integer> entry : weights.entrySet()) {
            String fieldName = getFieldDisplayName(entry.getKey());
            sb.append(fieldName).append(": ").append(entry.getValue()).append("% (우선순위)\n");
        }
        sb.append("\n 위 항목들을 우선적으로 고려하여 추천해주세요.\n");
        return sb.toString();
    }

    private String getFieldDisplayName(String field) {
        return switch (field) {
            case "smellLevel" -> "냄새 민감도";
            case "sleepLevel" -> "잠귀 민감도";
            case "tidinessLevel" -> "정리정돈 성향";
            case "bathroomCleaningLevel" -> "화장실 청소 빈도";
            case "cookingCount" -> "요리 빈도";
            case "alcoholCount" -> "음주 횟수";
            case "dishShare" -> "식기류 공유";
            case "phoneMode" -> "휴대폰 모드";
            case "earphoneUsage" -> "이어폰 사용";
            case "smoking" -> "흡연 여부";
            case "sleepHabit" -> "잠버릇";
            case "pet" -> "반려동물";
            default -> field;
        };
    }

    private List<UserRecommendationResDto> parseGptResponse(String gptResponse, List<User> candidates) {
        try {
            String jsonResponse = extractJsonFromResponse(gptResponse);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode recommendationsNode = rootNode.get("recommendations");

            List<UserRecommendationResDto> recommendations = new ArrayList<>();
            Map<Long, User> userMap = candidates.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            if (recommendationsNode != null && recommendationsNode.isArray()) {
                for (JsonNode recNode : recommendationsNode) {
                    if (recommendations.size() >= 10) break;

                    String userIdStr = recNode.get("userId").asText();
                    try {
                        Long userId = Long.parseLong(userIdStr);
                        User user = userMap.get(userId);
                        if (user != null) {
                            // reasonByItem -> reasonItems / reasonScores 로 분해
                            List<String> reasonItems = new ArrayList<>();
                            List<Integer> reasonScores = new ArrayList<>();
                            readReasonArrays(recNode, reasonItems, reasonScores);

                            recommendations.add(new UserRecommendationResDto(
                                    UserRecommendationResDto.UserBasicInfo.from(user),
                                    recNode.get("score").asInt(),
                                    reasonItems,
                                    reasonScores
                            ));
                        }
                    } catch (NumberFormatException ignore) {
                        // userId가 숫자가 아니면 스킵
                    }
                }
            }
            return recommendations;
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void readReasonArrays(JsonNode recNode,
                                  List<String> outItems,
                                  List<Integer> outScores) {
        JsonNode items = recNode.get("reasonItems");
        if (items != null && items.isArray()) {
            for (JsonNode it : items) {
                outItems.add(it.asText());
            }
        }

        JsonNode scores = recNode.get("reasonScores");
        if (scores != null && scores.isArray()) {
            for (JsonNode sc : scores) {
                // 정수만 허용, 0~100 범위로
                int v = sc.isNumber() ? sc.asInt() : 0;
                v = Math.max(0, Math.min(100, v));
                outScores.add(v);
            }
        }
    }

    private String extractJsonFromResponse(String response) {
        // gpt 응답에서 json만 추출
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");

        if(startIndex == -1 || endIndex == -1 || startIndex >= endIndex){
            throw new CustomException(ErrorStatus.RECOMMENDATION_SERVICE_ERROR);
        }

        return response.substring(startIndex, endIndex + 1);
    }

    private List<User> getCandidates(User currentUser) {
        // isRoom 조건 필터링
        if(currentUser.isRoom()){
            // 방이 있으면 -> 방 없는 사용자 추천
            return userRepository.findByIsRoomAndIdNot(false, currentUser.getId());
        } else {
            // 방이 없으면 -> 방 있는 사용자 추천
            return userRepository.findByIsRoomAndIdNot(true, currentUser.getId());
        }
    }

    private void extractReasonItemsAndScores(JsonNode reasonByItemNode,
                                             List<String> outItems,
                                             List<Integer> outScores) {
        if (reasonByItemNode == null || !reasonByItemNode.isObject()) return;

        Pattern scorePattern = Pattern.compile("일치도\\s*(\\d{1,3})\\s*/\\s*100");

        reasonByItemNode.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            String value = entry.getValue().asText();
            String koreanFieldName = getFieldDisplayName(key);

            // 표시용 아이템 문장
            outItems.add("• " + koreanFieldName + ": " + value);

            // 점수 추출
            Matcher m = scorePattern.matcher(value);
            if (m.find()) {
                try {
                    int score = Integer.parseInt(m.group(1));
                    // 0~100 범위로(혹시나 잘못 온 경우 안전장치)
                    score = Math.max(0, Math.min(100, score));
                    outScores.add(score);
                } catch (NumberFormatException ignore) {
                    // 점수 파싱 실패 시 추가하지 않음
                }
            }
        });
    }
}


