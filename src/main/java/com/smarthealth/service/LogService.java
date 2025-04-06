package com.smarthealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.common.result.PageResult;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.LogQueryDTO;
import com.smarthealth.domain.Entity.Log;

public interface LogService extends IService<Log> {
    //日志的分页查询
    Result LogPageQuery(LogQueryDTO loginLogQueryDTO);
}
