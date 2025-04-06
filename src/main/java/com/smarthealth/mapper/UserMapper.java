package com.smarthealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smarthealth.domain.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
