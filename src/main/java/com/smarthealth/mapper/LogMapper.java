package com.smarthealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smarthealth.domain.Entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
