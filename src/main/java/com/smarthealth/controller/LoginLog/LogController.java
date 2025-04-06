package com.smarthealth.controller.LoginLog;

import cn.hutool.core.bean.BeanUtil;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.LogQueryDTO;

import com.smarthealth.domain.Entity.Log;
import com.smarthealth.domain.VO.LogQueryVO;
import com.smarthealth.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping(value = "/sht/log")
public class LogController {

    @Resource
    private LogService loginLogService;

    /**
     * 分页条件查询登录日志列表
     */
    @PostMapping
    public Result list(@RequestBody LogQueryDTO logQueryDTO) {
        return loginLogService.LogPageQuery(logQueryDTO);
    }

    /**
     * 获取日志详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Log log = loginLogService.getById(id);
        if (log == null) {
            return Result.error("登录日志不存在");
        }
        return Result.ok(BeanUtil.copyProperties(log, LogQueryVO.class));
    }
}
