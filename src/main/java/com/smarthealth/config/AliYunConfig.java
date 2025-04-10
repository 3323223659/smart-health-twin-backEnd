package com.smarthealth.config;
import com.aliyun.ocr_api20210707.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author WyH524
 * @since 2025/4/6 上午10:30
 */

@Configuration
public class AliYunConfig {

    @Value("${aliyun.ocr.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.ocr.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.ocr.endpoint}")
    private String endpoint;


    @Bean
    public Client ocrClient() throws Exception {
        Config config = new Config()
                // 从环境变量中取出 ACCESS_KEY_ID、ACCESS_KEY_SECRET
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;
        return new Client(config);
    }
}
