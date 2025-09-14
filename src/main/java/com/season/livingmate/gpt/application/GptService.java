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

    public String recommendUsers(String currentUserProfile, List<String> candidateProfiles, String weights) {
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
        
        String prompt = String.format("""
            사용자 추천을 해주세요.
            
            현재 사용자 프로필:
            %s
            
            후보자들:
            %s
            
            가중치 설정:
            %s
            
            사용 가능한 사용자 ID: %s
            
            반드시 다음 형식으로만 JSON 응답해주세요:
            {
                "recommendations": [
                    {
                        "userId": "실제존재하는사용자ID",
                        "score": "85",
                        "reason": "추천 이유"
                    }
                ]
            }
                
            중요사항:
            - userId는 반드시 위에 나열된 사용 가능한 사용자 ID 중 하나여야 합니다
            - 사용 가능한 사용자 ID: %s
            - 최대 10명까지만 추천해주세요
            - 매칭 점수는 0-100 사이의 정수 문자열로 해주세요
            - 추천 이유는 구체적이고 개인화된 내용으로 작성해주세요
            - 가중치가 높은 항목일수록 더 중요하게 고려해주세요
            - 응답은 반드시 JSON 형식이어야 하며, 다른 설명이나 텍스트는 포함하지 마세요
            """, currentUserProfile, String.join("\n\n", candidateProfiles), weights, userIdList, userIdList);

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
