package com.smarthealth.domain.Chat;

import lombok.Data;

import java.util.List;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:37
 */
@Data
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;
}
