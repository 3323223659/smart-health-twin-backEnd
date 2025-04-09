package com.smarthealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smarthealth.domain.Entity.HealthAdvice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthAdviceMapper extends BaseMapper<HealthAdvice> {
}
