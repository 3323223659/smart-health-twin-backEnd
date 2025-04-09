package com.smarthealth.domain.Chat;

import lombok.Data;

import java.util.List;

/**
 * @author WyH524
 * @since 2025/4/9 下午8:29
 */
@Data
public class DashScopeRequest {
    private String model = "qwen-plus"; // 固定为qwen-plus
    private Input input;
    private Parameters parameters;

    @Data
    public static class Input {
        private List<ChatMessage> messages; // 对话历史
    }

    @Data
    public static class Parameters {
        private boolean stream = true; // 固定为流式输出
        private Integer max_tokens = 100; // 可选，默认100
    }

}
