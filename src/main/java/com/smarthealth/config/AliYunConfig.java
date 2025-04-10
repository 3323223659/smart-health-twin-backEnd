package com.smarthealth.config;
import com.aliyun.ocr_api20210707.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author WyH524
 * @since 2025/4/6 上午10:30
 */

@Configuration
public class AliYunConfig {
    @Bean
    public Client ocrClient() throws Exception {
        Config config = new Config()
                // 从环境变量中取出 ACCESS_KEY_ID、ACCESS_KEY_SECRET
                .setAccessKeyId(System.getenv("OSS_ACCESS_KEY_ID"))
                .setAccessKeySecret(System.getenv("OSS_ACCESS_KEY_SECRET"));
        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
        return new Client(config);
    }
}
