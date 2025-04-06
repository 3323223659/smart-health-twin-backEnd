package com.smarthealth.domain.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserQueryVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 手机号,不为空
     */
    private String phone;

    /**
     * 账号状态,默认为1启用,0禁用
     */
    private Integer status;

    /**
     * 用户类型:0超级管理员,1管理员,2用户,3vip用户
     */
    private Integer role;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;
}
