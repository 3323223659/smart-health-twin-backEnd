package com.smarthealth.domain.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author WyH524
 * @since 2025/4/5 下午10:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("health_report")
public class HealthReport {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //用户ID
    private Long userId;

    //报告日期
    private LocalDate reportDate;

    //血糖
    private Double bloodSugar;

    //葡萄糖
    private Double glucose;

    //血压
    private String bloodPressure;

    //总胆固醇
    private Double cholesterol;

    //甘油三脂
    private Double triglycerides;

    //白细胞
    private String whiteBloodCells;

    //报告图片路径
    private String photoPath;

    //报告全部信息
    private String fullReport;

    //红细胞
    private String hemoglobin;

    //上传时间
    private LocalDateTime createTime;
}
