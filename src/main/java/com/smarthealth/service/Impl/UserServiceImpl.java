package com.smarthealth.service.Impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smarthealth.common.Utils.JwtUtils;
import com.smarthealth.common.Utils.PasswordUtils;
import com.smarthealth.common.result.PageResult;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.UserDTO;
import com.smarthealth.domain.DTO.UserQueryDTO;
import com.smarthealth.domain.Entity.HealthReport;
import com.smarthealth.domain.Entity.Log;
import com.smarthealth.domain.Entity.User;
import com.smarthealth.domain.Entity.UserInfo;
import com.smarthealth.domain.VO.UserInfoQueryVO;
import com.smarthealth.domain.VO.UserQueryVO;
import com.smarthealth.mapper.UserMapper;
import com.smarthealth.service.HealthReportService;
import com.smarthealth.service.LogService;
import com.smarthealth.service.UserInfoService;
import com.smarthealth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;


    @Resource
    private LogService loginLogService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private HealthReportService healthReportService;


    /**
     * 根据手机号查找账户
     */
    public User getUserByPhone(String phone) {
        return getOne(new LambdaUpdateWrapper<User>().eq(User::getPhone, phone).eq(User::getIsDeleted,0));
    }


    /**
     * 根据手机号登录账户
     */
    @Override
    @Transactional
    public Result loginByPhone(UserDTO userDTO) {
        User user = getUserByPhone(userDTO.getPhone());
        if(user == null || !PasswordUtils.md5WithSalt(userDTO.getPassword()).equals(user.getPassword())){
            if(user != null){
                loginLogService.save(Log.builder()
                        .userId(user.getId())
                        .userType(user.getRole())
                        .logType("登录日志")
                        .operationTime(LocalDateTime.now())
                        .operationContent("密码错误,登录失败")
                        .build());
            }
            return Result.error("用户不存在或密码错误,请重新尝试");
        }
        //添加登录成功的日志
        loginLogService.save(Log.builder()
                .userId(user.getId())
                .userType(user.getRole())
                .operationTime(LocalDateTime.now())
                .logType("登录日志")
                .operationContent("登录成功")
                .build());

        //更新用户最后登录时间
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>().eq(User::getId, user.getId()) // 条件：user_id = userId
                .set(User::getLastLoginTime, LocalDateTime.now());// 设置字段值
        userMapper.update(updateWrapper);
        return Result.ok(JwtUtils.generateToken("user", Map.of("userid", user.getId())));
    }


    /**
     * 用户的分页条件查询
     */
    public Result userPageQuery(UserQueryDTO userQueryDTO) {
        Page<User> p = Page.of(userQueryDTO.getPage(), userQueryDTO.getPageSize());

        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(userQueryDTO.getRole() != null, User::getRole, userQueryDTO.getRole())
                .eq(userQueryDTO.getStatus() != null, User::getStatus, userQueryDTO.getStatus())
                .like(!StringUtils.isEmpty(userQueryDTO.getPhone()), User::getPhone, userQueryDTO.getPhone())
                .eq(User::getIsDeleted, 0);

        Page<User> resultPage = page(p, LambdaQueryWrapper);
        List<UserQueryVO> list = resultPage.getRecords().stream().map(user -> {
            return BeanUtil.copyProperties(user, UserQueryVO.class);
        }).toList();
        return Result.ok(new PageResult(resultPage.getTotal(), list));
    }


    //用户信息管理的分页查询
    public Result getUserInfoList(UserQueryDTO userQueryDTO) {
        Page<User> p = Page.of(userQueryDTO.getPage(), userQueryDTO.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getIsDeleted, 0)
                .eq(userQueryDTO.getRole() != null, User::getRole, userQueryDTO.getRole())
                .eq(userQueryDTO.getStatus() != null, User::getStatus, userQueryDTO.getStatus())
                .like(!StringUtils.isEmpty(userQueryDTO.getPhone()), User::getPhone, userQueryDTO.getPhone());

        Page<User> resultPage = page(p, lambdaQueryWrapper);
        List<UserInfoQueryVO> userInfoQueryVOList = resultPage.getRecords().stream().map(user -> {
            UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getId, user.getId())
                    .eq(UserInfo::getIsDeleted, 0));
            long reportCount = healthReportService.count(new LambdaQueryWrapper<HealthReport>().eq(HealthReport::getUserId, user.getId()));

            if(one == null){
                UserInfoQueryVO vo = UserInfoQueryVO.builder()
                        .username("user_"+getRandomDigitsFromTimestamp())
                        .reportCount(reportCount)
                        .status(1)
                        .build();
                vo.setPhone(user.getPhone());
                if(reportCount==0){
                    vo.setExaminationTime(null);
                    return vo;
                }
                LocalDateTime createTime = healthReportService.getOne(new LambdaQueryWrapper<HealthReport>().eq(HealthReport::getUserId, user.getId())
                        .orderByDesc(HealthReport::getCreateTime).last("LIMIT 1")).getCreateTime();
                vo.setExaminationTime(createTime);
                return vo;
            }

            UserInfoQueryVO vo = UserInfoQueryVO.builder()
                    .username(one.getUsername())
                    .phone(user.getPhone())
                    .reportCount(reportCount)
                    .status(one.getHealthStatus())
                    .build();
            if(reportCount==0){
                vo.setExaminationTime(null);
                return vo;
            }
            LocalDateTime createTime = healthReportService.getOne(new LambdaQueryWrapper<HealthReport>().eq(HealthReport::getUserId, user.getId())
                    .orderByDesc(HealthReport::getCreateTime).last("LIMIT 1")).getCreateTime();
            vo.setExaminationTime(createTime);
            return vo;
        }).toList();

        return Result.ok(new PageResult(resultPage.getTotal(), userInfoQueryVOList));
    }




    public static String getRandomDigitsFromTimestamp() {
        // 1. 获取当前时间戳（毫秒）
        long timestamp = System.currentTimeMillis(); // 示例：1681234567890

        // 2. 转为字符串
        String timestampStr = String.valueOf(timestamp);

        // 3. 随机选 3 位
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(timestampStr.length());
            result.append(timestampStr.charAt(randomIndex));
        }
        return result.toString(); // 示例结果："845"
    }

}
