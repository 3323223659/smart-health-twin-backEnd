package com.smarthealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smarthealth.domain.Entity.HealthReport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthReportMapper extends BaseMapper<HealthReport> {
}
