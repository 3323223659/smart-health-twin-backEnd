package com.smarthealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smarthealth.domain.Entity.UserInfo;
import com.smarthealth.domain.VO.CategoryByAgeVO;
import com.smarthealth.domain.VO.GenderVO;

public interface UserInfoService extends IService<UserInfo> {

    GenderVO getGenderNum();

    CategoryByAgeVO categoryByAge();
}
