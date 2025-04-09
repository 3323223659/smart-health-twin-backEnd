package com.smarthealth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smarthealth.domain.Chat.ChatCompletionRequest;
import com.smarthealth.domain.Chat.ChatCompletionResponse;
import com.smarthealth.domain.Chat.ChatMessage;
import com.smarthealth.domain.Chat.OpenAIClient;
import com.smarthealth.domain.Entity.HealthAdvice;
import com.smarthealth.service.ChatService;
import com.smarthealth.service.HealthAdviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:43
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final HealthAdviceService healthAdviceService;

    private final OpenAIClient openAIClient;

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 前缀
    private static final String CHAT_HISTORY_KEY_PREFIX = "chat:history:";


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
        // 获取或初始化会话历史
        List<ChatMessage> conversationHistory = getConversationHistory(historyKey);

        if (conversationHistory.isEmpty()) {
            conversationHistory.add(getSystemMessage()); // 首次会话添加系统消息
            redisTemplate.opsForList().rightPush(historyKey,getSystemMessage());
        }

        // 添加用户消息到历史
        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        conversationHistory.add(userMsg);

        // 构建请求
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(new ArrayList<>(conversationHistory));
        //创建了一个全新的 ArrayList 对象,将 conversationHistory 中的所有元素浅拷贝到新列表中,两个列表相互独立(修改新列表不会影响原列表)

        // 调用阿里云API，获取AI回复
        String aiResponse = openAIClient.createChatCompletion(request);

        // 7. 直接存储AI回复到Redis(仅新增部分)
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        redisTemplate.opsForList().rightPush(historyKey, userMsg);
        redisTemplate.opsForList().rightPush(historyKey, aiMsg);
        redisTemplate.expire(historyKey, 7, TimeUnit.DAYS);//设置过期时间，7天

        return aiResponse;
    }



    // 新增流式输出方法
    public InputStream chatStream(String userId, String userMessage) throws IOException {
        String historyKey = CHAT_HISTORY_KEY_PREFIX + userId;
        log.info("Chat method - User ID: " + userId + ", Message: " + userMessage);

        List<ChatMessage> conversationHistory = getConversationHistory(historyKey);
        if (conversationHistory.isEmpty()) {
            conversationHistory.add(getSystemMessage()); // 首次会话添加系统消息
            redisTemplate.opsForList().rightPush(historyKey,getSystemMessage());
        }

        log.info("历史信息多少条: " + conversationHistory.size());
        if (conversationHistory.isEmpty()) {
            conversationHistory.add(getSystemMessage());
        }

        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        redisTemplate.opsForList().rightPush(historyKey,userMsg);
        conversationHistory.add(userMsg);

        // 直接调用新的流式方法
        return openAIClient.createChatCompletionStream(conversationHistory);
    }


    //将流式输出的结果保存到redis
    public void saveStreamResponse(String userId, String aiResponse) {
        String historyKey = CHAT_HISTORY_KEY_PREFIX + userId;
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        redisTemplate.opsForList().rightPush(historyKey,aiMsg);
        redisTemplate.expire(historyKey, 7, TimeUnit.DAYS);
    }


    // 新增体检报告分析方法
    public String analyzeReport(String reportText,Long userId) throws IOException {

        // 定制提示词，要求大模型分析体检报告并返回结构化结果
        String prompt = "以下是一段未格式化的体检报告文本，请分析其关键信息，异常项及建议，保持回答精炼，200字以内。体检报告文本：" + reportText;

        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(prompt);
        ArrayList<ChatMessage> list = new ArrayList<>();
        list.add(userMsg);

        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(list);

        String analysisResult = openAIClient.createChatCompletion(request);
        HealthAdvice healthAdvice = healthAdviceService.getOne(new LambdaQueryWrapper<HealthAdvice>().eq(HealthAdvice::getUserId, userId));
        if(healthAdvice==null){
            if(healthAdviceService.save(HealthAdvice.builder()
                    .userId(userId)
                    .bodyAdvice(analysisResult)
                    .createTime(LocalDateTime.now())
                    .build()))
                return analysisResult;
            return null;
        }
        if(healthAdviceService.saveOrUpdate(HealthAdvice.builder()
                .id(healthAdvice.getId())
                .bodyAdvice(analysisResult)
                .build())){
            return analysisResult;
        }
        return null;
    }




    // 清空对话历史
    public void clearHistory(String userid) {
        String historyKey = CHAT_HISTORY_KEY_PREFIX +userid;
        redisTemplate.delete(historyKey);
    }


    // 从 Redis 获取会话历史
    private List<ChatMessage> getConversationHistory(String historyKey) {
        //全部的会话历史
        List<Object> history = redisTemplate.opsForList().range(historyKey, 0, -1);
        if (history == null || history.isEmpty()) {
            return new ArrayList<>();
        }
        //把每一个对象强转为对应对象
        return history.stream().map(obj -> (ChatMessage) obj)
                .collect(Collectors.toList());
    }
}
