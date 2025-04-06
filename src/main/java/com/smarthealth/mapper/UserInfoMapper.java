package com.smarthealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smarthealth.domain.Entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
