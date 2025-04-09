package com.smarthealth.domain.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


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

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAIClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .build();
    }

    public String createChatCompletion(ChatCompletionRequest request) throws Exception {
        request.setModel(model);  // 设置模型

        String requestBody = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("API调用失败，状态码: " + response.statusCode() + ", 响应: " + response.body());
        }

        String responseBody = response.body();
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


    // 支持流式响应的方法（后续用于流输出）
    public InputStream createChatCompletionStream(ChatCompletionRequest request) throws Exception{
        request.setModel(model);

        String requestBody = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/chat/completions")) // 假设支持流式接口，可能需要调整URL
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "text/event-stream") // 流式响应需要此头
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            throw new IOException("API调用失败，状态码: " + response.statusCode());
        }
        return response.body();
    }

}
