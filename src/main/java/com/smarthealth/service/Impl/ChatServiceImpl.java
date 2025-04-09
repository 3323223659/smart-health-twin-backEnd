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

    // Redis key 前缀
    private static final String CHAT_HISTORY_KEY_PREFIX = "chat:history:";

    // 初始化时设置AI身份，Bean实例化完成后执行
    @PostConstruct
    public void init() {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent("你是一个由air实验室开发的智慧医疗助手，名字叫做小A，你可以为我提供专业的医疗健康咨询、疾病就诊建议、用药指导以及" +
                "健康管理方案等帮助，请保持回答精炼，控制在200字以内");
        conversationHistory.add(systemMessage);
    }

    // 初始化系统消息，首次创建用户会话时添加
    private ChatMessage getSystemMessage() {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent("你是一个由air实验室开发的智慧医疗助手，名字叫做小A，" +
                "你可以为我提供专业的医疗健康咨询、疾病就诊建议、用药指导以及健康管理方案等帮助。" +
                "请保持回答精炼，控制在200字以内。");
        return systemMessage;
    }



    public String chat(String userMessage , String userid) throws IOException {

        String historyKey =CHAT_HISTORY_KEY_PREFIX + userid;

        // 添加用户消息到历史
        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        conversationHistory.add(userMsg);

        // 构建请求
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(new ArrayList<>(conversationHistory));
        //max_tokens 默认已在 ChatCompletionRequest 中设置为 50，无需额外设置
        //创建了一个全新的 ArrayList 对象,将 conversationHistory 中的所有元素浅拷贝到新列表中,两个列表相互独立（修改新列表不会影响原列表）

        // 调用阿里云API，获取AI回复
        String aiResponse = openAIClient.createChatCompletion(request);

        // 添加AI回复到历史
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        conversationHistory.add(aiMsg);

        return aiResponse;
    }


    // 清空对话历史
    public void clearHistory() {
        // 保留system消息
        ChatMessage systemMessage = conversationHistory.get(0);
        conversationHistory.clear();
        conversationHistory.add(systemMessage);
    }
}
