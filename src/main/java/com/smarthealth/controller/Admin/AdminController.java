package com.smarthealth.controller.Admin;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smarthealth.common.result.PageResult;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.UserQueryDTO;
import com.smarthealth.domain.Entity.UserInfo;
import com.smarthealth.domain.VO.GenderVO;
import com.smarthealth.domain.VO.UserInfoVO;
import com.smarthealth.service.UserInfoService;
import com.smarthealth.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sht/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;


    //用户的条件分页查询
    @PostMapping("/user/list")
    public Result getUserList(@RequestBody UserQueryDTO userQueryDTO){
        return userService.userPageQuery(userQueryDTO);
    }


    //根据所选择的用户id获取用户信息
    @GetMapping("/user/info/{id}")
    public Result getUserInfoById(@PathVariable Long id){
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", id).eq("is_deleted", 0));
        if(userInfo==null){
            return Result.error("该用户信息不存在");
        }
        UserInfoVO userInfoVO = BeanUtil.copyProperties(userInfo, UserInfoVO.class);
        return Result.ok(userInfoVO);
    }



    //统计男性，女性各有多少人
    @GetMapping("/user/sex")
    public Result getUserSex(){
        return Result.ok(userInfoService.getGenderNum());
    }


    //返回各年龄段的人数
    @GetMapping("/user/age")
    public Result categoryByAge(){
         return Result.ok(userInfoService.categoryByAge());
    }

}
