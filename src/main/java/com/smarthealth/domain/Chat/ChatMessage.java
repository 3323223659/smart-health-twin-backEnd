package com.smarthealth.domain.Chat;

import lombok.Data;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:36
 */
@Data
public class ChatMessage {
    // 角色，例如 "user" 或 "assistant"
    private String role;

    // 消息内容
    private String content;
}
