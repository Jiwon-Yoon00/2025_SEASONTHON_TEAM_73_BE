package com.season.livingmate.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GptConfig {

    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.url}")
    private String apiUrl;

    @Value("${gpt.model}")
    private String model;
}
