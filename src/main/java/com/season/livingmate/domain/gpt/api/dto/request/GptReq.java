package com.season.livingmate.domain.gpt.api.dto.request;

import java.util.List;

public record GptReq(
        String model,
        List<Message> messages
) {
    public record Message(
            String role,
            String content
    ) {}
}
