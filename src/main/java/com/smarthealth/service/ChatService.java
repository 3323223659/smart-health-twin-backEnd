package com.smarthealth.service;

import java.io.IOException;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:46
 */
public interface ChatService {
    public String chat(String userMessage) throws IOException;

    public void clearHistory();
}
