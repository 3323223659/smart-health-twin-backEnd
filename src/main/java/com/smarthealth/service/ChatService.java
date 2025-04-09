package com.smarthealth.service;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author WyH524
 * @since 2025/4/6 下午12:46
 */
public interface ChatService {

    //清除流式输出
    void clearHistory(String userid);

    //正常输出聊天
    String chat( String userMessage ,String userid) throws Exception;

    //流式输出聊天
    InputStream chatStream(String userId, String userMessage) throws IOException;

    //保存流式输出回复
    void saveStreamResponse(String userId, String aiResponse);

    // 新增体检报告分析方法
    String analyzeReport(String reportText,Long userId) throws IOException;
}
