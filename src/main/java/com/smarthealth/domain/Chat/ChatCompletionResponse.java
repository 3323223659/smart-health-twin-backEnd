package com.smarthealth.domain.Chat;

import lombok.Data;

import java.util.List;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:38
 */
@Data
public class ChatCompletionResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private ChatMessage message;
        private String finish_reason;
    }
}
