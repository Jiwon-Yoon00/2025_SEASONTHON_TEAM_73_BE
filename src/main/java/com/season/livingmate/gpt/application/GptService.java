package com.season.livingmate.gpt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.config.GptConfig;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.gpt.api.dto.request.GptRequest;
import com.season.livingmate.gpt.api.dto.response.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GptService {

    private final GptConfig gptConfig;
    private final ObjectMapper objectMapper;
    private final OkHttpClient httpClient;

    public String recommendUsers(String currentUserProfile, List<String> candidateProfiles, String weights, List<String> selectedItems) {
        // 실제 사용자 ID 목록 추출
        List<String> userIds = new ArrayList<>();
        for (String profile : candidateProfiles) {
            String[] lines = profile.split("\n");
            if (lines.length > 0 && lines[0].startsWith("사용자 ID: ")) {
                String userId = lines[0].replace("사용자 ID: ", "").trim();
                userIds.add(userId);
            }
        }

        String userIdList = String.join(", ", userIds);

        String selectedItemsStr = String.join(", ", selectedItems);

        String prompt = String.format("""
            사용자 추천을 해주세요.
            
            현재 사용자 프로필:
            %s
            
            후보자들:
            %s
            
            가중치 설정:
            %s
            
            선택한 가중치 항목(selectedItems): %s
            
            사용 가능한 사용자 ID: %s
            
            반드시 다음 형식으로만 JSON 응답해주세요:
            {
                "recommendations": [
                    {
                        "userId": "실제존재하는사용자ID",
                        "score": "85",
                        "reasonByItem": {
                              "%s": "일치도 XX/100 — nickname: 핵심 근거 한 가지",
                              "%s": "일치도 XX/100 — nickname: 핵심 근거 한 가지",
                              "%s": "일치도 XX/100 — nickname: 핵심 근거 한 가지"
                        }
                    }
                ]
            }
                
            중요사항:
            - userId는 반드시 위에 나열된 사용 가능한 사용자 ID 중 하나여야 합니다
            - 사용 가능한 사용자 ID: %s
            - **반드시 정확히 10명을 추천해주세요**
            - 매칭 점수는 0-100 사이의 정수 문자열로 해주세요
            
            핵심 추천 원칙 (절대 우선순위):
            - **점수가 높은 순서대로 내림차순으로 정렬해서 추천해주세요**
            - **가장 높은 점수부터 1번째, 2번째, 3번째... 순서로 추천해주세요**
            - **반드시 10명을 모두 추천해주세요**
            - **일치도가 낮아도 점수가 높으면 추천하세요**
            
            정확한 점수 계산 규칙:
            - 총점 = (항목1_일치도 × 25 + 항목2_일치도 × 25 + 항목3_일치도 × 25 + 나머지_항목들_평균_일치도 × 25) / 100
            - 예: (100×25 + 80×25 + 60×25 + 70×25) / 100 = 77.5점 → 78점
            - 일치도가 낮으면 총점도 낮아야 합니다
            
            일치도 계산 규칙 (반드시 준수):
            - 선택된 가중치 3개 항목의 일치도를 우선적으로 계산하세요
            - 각 항목별 일치도: 0%% (완전 다름) ~ 100%% (완전 일치)
            - 가중치가 높은 항목일수록 더 중요하게 고려하세요
            - 나머지 성향조사 항목들도 일치도를 계산하되, 가중치 항목보다 낮은 우선순위로 고려하세요
            
            추천 이유 작성 규칙 (반드시 준수):
            - "reasonByItem"은 selectedItems 배열의 **각 요소를 키로 그대로 사용**합니다.
            - 반복 규칙: selectedItems의 각 item에 대해
              reasonByItem[item] = "일치도 NN/100 — {상대 nickname}: {왜 함께 살기 좋은지 핵심 근거 1가지(짧게)}"
            - 각 문장은 한 줄(20–80자 권장)로 간결하게 작성하세요.
            - 추천 이유에서 "사용자 ID", "ID", "사용자" 등의 표현을 절대 사용하지 마세요
            - 반드시 nickname만 사용해주세요
            - 왜 이 사용자와 함께 살기에 좋은지 구체적으로 설명해주세요
            - 가중치가 높은 항목들의 일치도를 중심으로 설명해주세요
            
            금지사항:
            - "사용자 ID 2", "ID 2", "사용자 2" 등의 표현 사용 금지
            - 모호하거나 일반적인 설명 금지
            - 일치도가 낮은 후보자 추천 금지
            
            최종 확인사항:
            - **정확히 10명이 추천되고 있나요?**
            - **점수가 높은 순서대로 정렬되어 있나요?**
            - **가장 높은 점수가 첫 번째에 있나요?**
            
            - 가중치가 높은 항목일수록 더 중요하게 고려해주세요
            - 응답은 반드시 JSON 형식이어야 하며, 다른 설명이나 텍스트는 포함하지 마세요
            """, currentUserProfile, String.join("\n\n", candidateProfiles), weights, selectedItemsStr, userIdList, 
            selectedItems.get(0), selectedItems.get(1), selectedItems.get(2), userIdList);

        return callGptApi(prompt);
    }

    private String callGptApi(String prompt) {
        try {
            GptRequest request = new GptRequest(
                    gptConfig.getModel(),
                    List.of(new GptRequest.Message("user", prompt))
            );

            String json = objectMapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request httpRequest = new Request.Builder()
                    .url(gptConfig.getApiUrl())
                    .addHeader("Authorization", "Bearer " + gptConfig.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(httpRequest).execute()){
                if (!response.isSuccessful()){
                    throw new CustomException(ErrorStatus.GPT_CALL_FAILED);
                }

                String responseBody = response.body().string();
                GptResponse gptResponse = objectMapper.readValue(responseBody, GptResponse.class);

                return gptResponse.choices().get(0).message().content();
            }
        } catch (IOException e){
            throw new CustomException(ErrorStatus.GPT_CALL_FAILED);
        }
    }
}
