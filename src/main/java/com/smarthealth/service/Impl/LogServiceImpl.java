package com.smarthealth.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smarthealth.common.result.PageResult;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.LogQueryDTO;
import com.smarthealth.domain.Entity.Log;
import com.smarthealth.domain.VO.LogQueryVO;
import com.smarthealth.mapper.LogMapper;
import com.smarthealth.service.LogService;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    // 因为这里继承了ServiceImpl，这个类里面已经注入了Mapper，所以说我是可以直接拿到这个Mapper的


    @Override
    public Result LogPageQuery(LogQueryDTO logQueryDTO) {
        Page<Log> p = new Page<>(logQueryDTO.getPage(), logQueryDTO.getPageSize());
        //条件查询
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<Log>()
                .like(logQueryDTO.getOperationContent()!=null, Log::getOperationContent,logQueryDTO.getOperationContent())
                .eq(logQueryDTO.getLogType()!=null,Log::getLogType,logQueryDTO.getLogType())
                .ge(logQueryDTO.getBeginTime() != null, Log::getOperationTime, logQueryDTO.getBeginTime())  //>大于
                .le(logQueryDTO.getEndTime() != null, Log::getOperationTime, logQueryDTO.getEndTime())
                .orderByDesc(Log::getOperationTime);

        Page<Log> resultPage = page(p,wrapper);

        List<Log> records = resultPage.getRecords();
        List<LogQueryVO> list = records.stream() // 将集合转换为 Stream
                .map(log -> {
                    return BeanUtil.copyProperties(log, LogQueryVO.class);
                })
                .toList();// 将结果收集为 List
        return Result.ok(new PageResult(resultPage.getTotal(), list));
    }



}
