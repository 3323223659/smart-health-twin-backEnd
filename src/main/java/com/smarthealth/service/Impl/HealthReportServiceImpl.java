package com.smarthealth.service.Impl;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeBasicRequest;
import com.aliyun.ocr_api20210707.models.RecognizeBasicResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smarthealth.common.Utils.OcrProcessor;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Chat.ChatCompletionRequest;
import com.smarthealth.domain.Chat.ChatMessage;
import com.smarthealth.domain.Entity.HealthReport;
import com.smarthealth.mapper.HealthReportMapper;
import com.smarthealth.service.HealthReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.aliyun.teautil.Common.assertAsString;

/**
 * @author WyH524
 * @since 2025/4/6 上午10:35
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport> implements HealthReportService {

    private final Client client;

    private final ChatServiceImpl chatService;

    //识别存储体检报告并将建议存到mysql中
    public Result recognize(String file, Long userId) {
        RecognizeBasicRequest recognizeBasicRequest = new RecognizeBasicRequest()
                .setUrl(file)
                .setNeedRotate(false);
        try {
            RecognizeBasicResponse response = client.recognizeBasicWithOptions(recognizeBasicRequest, new RuntimeOptions());
            // 提取并打印核心识别内容
            if (response.getBody() != null && response.getBody().getData() != null) {
                String recognizedText = response.getBody().getData();

                // 使用 OcrProcessor 处理 OCR 结果并封装到 HealthReport
                HealthReport report = OcrProcessor.processOcrResult(recognizedText, userId);
                report.setPhotoPath(file);
                System.out.println(report);
                if(save(report)){
                    //给出建议保存到数据库中
                    String fullReport = report.getFullReport();
                    log.info("体检建议："+chatService.analyzeReport(fullReport, userId));
                    return Result.ok("上传体检报告成功");
                }
                return Result.error("上传体检报告失败");
            } else {
                System.out.println("未识别到文本！");
            }

        } catch (TeaException error) {
            System.out.println("TeaException: " + error.getMessage());
            if (error.getData() != null) {
                System.out.println("Recommend: " + error.getData().get("Recommend"));
            } else {
                System.out.println("No recommendation available.");
            }
            assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            System.out.println("Exception: " + error.getMessage());
            if (error.getData() != null) {
                System.out.println("Recommend: " + error.getData().get("Recommend"));
            } else {
                System.out.println("No recommendation available.");
            }
            assertAsString(error.message);
        }
        return Result.error("上传体检报告失败");
    }

    //获取最新的体检报告
    public Result getNewReport(Long userId) {
        HealthReport healthReport = getOne(new LambdaQueryWrapper<HealthReport>()
                .eq(HealthReport::getUserId, userId)
                .orderByDesc(HealthReport::getCreateTime)
                .last("LIMIT 1"));
        if (healthReport != null) {
            return Result.ok(healthReport);
        }
        return Result.error("暂未发现体检报告记录,请录入");
    }


}
