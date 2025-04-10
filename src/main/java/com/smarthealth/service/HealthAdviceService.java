package com.smarthealth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Entity.HealthAdvice;

public interface HealthAdviceService extends IService<HealthAdvice> {
    //获取体检报告建议
    Result getHealthReportBodyAdvice(Long userId);
}
