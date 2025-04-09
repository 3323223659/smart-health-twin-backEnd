package com.smarthealth.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名,不为空
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 地址
     */
    private String address;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 性别:1男性,0女性
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 健康状态
     */
    private Integer healthStatus;
}
