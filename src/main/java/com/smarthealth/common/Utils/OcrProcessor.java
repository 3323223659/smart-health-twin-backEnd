package com.smarthealth.common.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthealth.domain.Entity.HealthReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WyH524
 * @since 2025/4/6 上午9:31
 */
public class OcrProcessor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 从 OCR 返回的 JSON 数据中提取字段并封装到 HealthReport
     *
     * @param ocrJson OCR 返回的 JSON 数据
     * @param userId  用户 ID
     * @return 封装好的 HealthReport
     * @throws Exception 如果解析失败
     */
    public static HealthReport processOcrResult(String ocrJson, Long userId) throws Exception {
        HealthReport report = new HealthReport();
        report.setUserId(userId);
        report.setReportDate(LocalDate.now());
        report.setCreateTime(LocalDateTime.now());

        // 直接解析传入的 ocrJson（已经是 response.getBody().getData() 的内容）
        JsonNode dataNode = objectMapper.readTree(ocrJson);
        String content = dataNode.get("content").asText();

        // 使用正则表达式提取字段
        extractField(content, "血压：[\\s]*(\\d+/\\d+\\s*mmHg)", report::setBloodPressure);
        extractField(content, "血糖：[\\s]*血糖[\\s]*\\(?(\\d+\\.\\d+)\\s*mmol/L\\)?", value -> report.setBloodSugar(Double.parseDouble(value)));
        extractField(content, "葡萄糖\\s*\\(?(\\d+\\.\\d+)\\s*mmol/L\\)?", value -> report.setGlucose(Double.parseDouble(value)));
        extractField(content, "总胆固醇\\s*\\(?(\\d+\\.\\d+)\\s*mmol/L\\)?", value -> report.setCholesterol(Double.parseDouble(value)));
        extractField(content, "甘油三脂\\s*\\(?(\\d+\\.\\d+)\\s*mmol/L\\)?", value -> report.setTriglycerides(Double.parseDouble(value)));
        extractField(content, "白细胞\\s*\\(?([\\d.]+/10-9/L)\\)?", report::setWhiteBloodCells);
        extractField(content, "红细胞\\s*\\(?([\\d.]+/10-12/L)\\)?", report::setHemoglobin);
        return report;
    }

    /**
     * 使用正则表达式提取字段并设置到 HealthReport
     *
     * @param content 文本内容
     * @param regex   正则表达式
     * @param setter  设置方法
     */
    private static void extractField(String content, String regex, java.util.function.Consumer<String> setter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            setter.accept(matcher.group(1));
        }
    }
}
