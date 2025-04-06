package com.smarthealth.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogQueryVO {
    // 日志ID，自增主键
    private Integer id;

    // 用户/管理员ID
    private Integer userId;

    //用户名称
    private String userName;

    // 用户类型:0超级管理员,1管理员,2用户,3vip用户
    private Integer userType;

    // 日志类型
    private String logType;

    //日志内容
    private String operationContent;

    // 登录时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;
}
