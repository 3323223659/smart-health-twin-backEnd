package com.smarthealth.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author WyH524
 * @since 2025/4/5 下午7:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableName("log")
public class Log {
    //日志主键id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //用户id
    private Long userId;

    //用户类型
    private Integer userType;

    //日志类型
    private String logType;

    //操作内容
    private String operationContent;

    //操作时间
    private LocalDateTime operationTime;

    //日志详细信息
    private String details;
}
