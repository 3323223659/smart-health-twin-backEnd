package com.smarthealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Entity.HealthReport;

public interface HealthReportService extends IService<HealthReport> {
    //识别存储体检报告并将体检信息存到mysql
    Result recognize(String file, Long userId);

    //获取最新体检报告
    Result getNewReport(Long userId);
}
