package com.smarthealth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Entity.HealthAdvice;
import com.smarthealth.mapper.HealthAdviceMapper;
import com.smarthealth.service.HealthAdviceService;
import org.springframework.stereotype.Service;

/**
 * @author WyH524
 * @since 2025/4/9 下午11:14
 */
@Service
public class HealthAdviceServiceImpl extends ServiceImpl<HealthAdviceMapper, HealthAdvice> implements HealthAdviceService {

    public Result getHealthReportBodyAdvice(Long userId) {
        HealthAdvice healthAdvice = getOne(new LambdaQueryWrapper<HealthAdvice>()
                .eq(HealthAdvice::getUserId, userId));
        if(healthAdvice==null){
            return Result.ok("");
        }
        return Result.ok(healthAdvice.getBodyAdvice());
    }

}
