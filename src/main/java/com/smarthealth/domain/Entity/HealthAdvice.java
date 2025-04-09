package com.smarthealth.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author WyH524
 * @since 2025/4/9 下午10:59
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_advice")
public class HealthAdvice {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String bodyAdvice;

    private String sleepAdvice;

    private String dietAdvice;

    private String mentalAdvice;

    private LocalDateTime createTime;
}
