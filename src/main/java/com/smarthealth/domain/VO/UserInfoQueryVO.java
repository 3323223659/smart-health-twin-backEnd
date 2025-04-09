package com.smarthealth.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author WyH524
 * @since 2025/4/9 下午1:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoQueryVO {
    private String userName;

    private String phone;

    private Long reportCount;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime examinationTime;
}
