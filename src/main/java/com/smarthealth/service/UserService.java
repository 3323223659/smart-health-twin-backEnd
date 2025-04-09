package com.smarthealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.common.result.PageResult;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.UserDTO;
import com.smarthealth.domain.DTO.UserQueryDTO;
import com.smarthealth.domain.Entity.User;
import com.smarthealth.domain.VO.GenderVO;

public interface UserService extends IService<User> {


    User getUserByPhone(String phone);

    //用户根据手机号和密码进行登录
    Result loginByPhone(UserDTO userDTO);


    Result userPageQuery(UserQueryDTO userQueryDTO);

    Result getUserInfoList(UserQueryDTO userQueryDTO);
}
