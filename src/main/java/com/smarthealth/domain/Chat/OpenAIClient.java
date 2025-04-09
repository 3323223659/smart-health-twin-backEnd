package com.smarthealth.domain.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author WyH524
 * @since 2025/4/6 下午12:39
 */
@Component
@Data
public class OpenAIClient {
    @Value("${aliyun.dashscope.api-key}")
    private String apiKey;

    @Value("${aliyun.dashscope.base-url}")
    private String baseUrl;

    @Value("${aliyun.dashscope.model-name}")
    private String model;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAIClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)  // 连接超时设置为60秒
                .readTimeout(120, TimeUnit.SECONDS)   // 读取超时设置为120秒
                // 表示从服务器读取数据的最长等待时间。大模型生成回答可能需要较长时间，因此设置较长。
                .writeTimeout(60, TimeUnit.SECONDS)   // 写入超时设置为60秒
                .build();
    }


    public String createChatCompletion(ChatCompletionRequest request) throws IOException {
        request.setModel(model);  // 设置模型

        // 将 max_tokens 放入 parameters 对象（DashScope HTTP API 要求）
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", request.getModel());
        requestMap.put("messages", request.getMessages());
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("max_tokens", request.getMax_tokens());
        requestMap.put("parameters", parameters);


        // 构建请求体
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(request),
                MediaType.get("application/json; charset=utf-8"));

        // 构建HTTP请求
        Request httpRequest = new Request.Builder()
                .url(baseUrl + "/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        // 发送请求并处理响应
        try (Response response = okHttpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API调用失败，状态码: " + response.code() + ", 响应: " + response.body().string());
            }

            // 获取响应体
            String responseBody = response.body().string();
            // 调试：打印原始响应
            System.out.println("API Response: " + responseBody);

            // 解析JSON，提取 choices[0].message.content
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.path("message");
                JsonNode contentNode = messageNode.path("content");
                if (contentNode.isTextual()) {
                    return contentNode.asText();
                } else {
                    throw new IOException("响应中未找到有效的 message.content 字段");
                }
            } else {
                throw new IOException("响应中未找到 choices 字段或 choices 为空");
            }
        }
    }
}