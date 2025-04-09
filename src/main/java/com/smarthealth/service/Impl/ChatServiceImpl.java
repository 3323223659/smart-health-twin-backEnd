package com.smarthealth.service.Impl;

import com.smarthealth.domain.Chat.ChatCompletionRequest;
import com.smarthealth.domain.Chat.ChatCompletionResponse;
import com.smarthealth.domain.Chat.ChatMessage;
import com.smarthealth.domain.Chat.OpenAIClient;
import com.smarthealth.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:43
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final OpenAIClient openAIClient;

    private final RedisTemplate<String, Object> redisTemplate;

    // 存储对话历史,后期改进将数据存储到Redis中或者Mysql）
    private final List<ChatMessage> conversationHistory = new ArrayList<>();

    private static final String CHAT_HISTORY_KEY_PREFIX = "chat:history:";

    // 初始化时设置AI身份，Bean实例化完成后执行
    @PostConstruct
    public void init() {
//        ChatMessage systemMessage = new ChatMessage();
//        systemMessage.setRole("system");
//        systemMessage.setContent("你是一个由air实验室开发的智慧医疗助手，名字叫做小A，你可以为我提供专业的医疗健康咨询、疾病就诊建议、用药指导以及" +
//                "健康管理方案等帮助");
//        conversationHistory.add(systemMessage);
    }

    public String chat(String userId, String userMessage) throws Exception {
        String historyKey = CHAT_HISTORY_KEY_PREFIX + userId;

        // 获取或初始化会话历史
        List<ChatMessage> conversationHistory = getConversationHistory(historyKey);
        if (conversationHistory.isEmpty()) {
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRole("system");
            systemMessage.setContent("你是一个由air实验室开发的智慧医疗助手，名字叫做小A，" +
                    "你可以为我提供专业的医疗健康咨询、疾病就诊建议、用药指导以及健康管理方案等帮助，" +
                    "请控制回答字数在100字以内。");
            conversationHistory.add(systemMessage);
        }

        // 添加用户消息
        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        conversationHistory.add(userMsg);

        // 构建请求
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(new ArrayList<>(conversationHistory));

        // 调用API获取回复
        String aiResponse = openAIClient.createChatCompletion(request);

        // 添加AI回复到历史
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        conversationHistory.add(aiMsg);

        // 保存到Redis
        redisTemplate.opsForList().rightPushAll(historyKey, conversationHistory.toArray());
        redisTemplate.expire(historyKey, 1, TimeUnit.HOURS); // 设置过期时间，例如1小时

        return aiResponse;
    }

    public void clearHistory(String userId) {
        String historyKey = CHAT_HISTORY_KEY_PREFIX + userId;
        redisTemplate.delete(historyKey);
    }

    private List<ChatMessage> getConversationHistory(String historyKey) {
        //获取 historyKey 对应的整个列表的所有元素
        List<Object> history = redisTemplate.opsForList().range(historyKey, 0, -1);
        if (history == null || history.isEmpty()) {
            return new ArrayList<>();
        }
        return history.stream()
                .map(obj -> (ChatMessage) obj)
                .collect(Collectors.toList());
    }

    // 流式输出方法
    public InputStream chatStream(String userId, String userMessage) throws Exception {
        String historyKey = CHAT_HISTORY_KEY_PREFIX + userId;
        List<ChatMessage> conversationHistory = getConversationHistory(historyKey);
        if (conversationHistory.isEmpty()) {
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRole("system");
            systemMessage.setContent("你是一个由air实验室开发的智慧医疗助手，名字叫做小A，" +
                    "你可以为我提供专业的医疗健康咨询、疾病就诊建议、用药指导以及健康管理方案等帮助，" +
                    "请控制回答字数在100字以内。");
            conversationHistory.add(systemMessage);
        }

        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        conversationHistory.add(userMsg);

        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(new ArrayList<>(conversationHistory));

        // 返回流式响应
        return openAIClient.createChatCompletionStream(request);
    }


}
