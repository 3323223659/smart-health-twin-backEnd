package com.smarthealth.service;

import com.smarthealth.domain.Chat.ChatMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:46
 */
public interface ChatService {

    void clearHistory(String userId);

    String chat(String userId, String userMessage) throws Exception;

    InputStream chatStream(String userId, String userMessage) throws Exception;
}
