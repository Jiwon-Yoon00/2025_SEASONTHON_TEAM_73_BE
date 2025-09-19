package com.season.livingmate.gpt.api.dto.request;

import java.util.List;

public record GptRequest(
        String model,
        List<Message> messages
) {
    public record Message(
            String role,
            String content
    ) {}
}
