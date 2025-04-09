package com.smarthealth.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQueryDTO {

    //当前页面
    private int page;

    //一页所展示数据的数据条数
    private int pageSize;

    //手机号
    private String phone;

    /**
     * 账号状态,默认为1启用,0禁用
     */
    private Integer status;

    /**
     * 用户类型:0超级管理员,1管理员,2用户,3vip用户
     */
    private Integer role;
}
