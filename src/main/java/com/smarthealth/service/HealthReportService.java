package com.smarthealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Entity.HealthReport;

public interface HealthReportService extends IService<HealthReport> {
    //识别存储体检报告
    Result recognize(String file, Long userId);
}
