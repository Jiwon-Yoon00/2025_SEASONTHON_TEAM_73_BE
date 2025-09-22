package com.season.livingmate.domain.gpt.api.dto.response;

import java.util.List;

public record GptRes(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ){
        public record Message(
                String content
        ){}
    }
}
