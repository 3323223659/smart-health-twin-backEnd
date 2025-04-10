package com.smarthealth.controller.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smarthealth.common.Utils.AliOssUtil;
import com.smarthealth.common.Utils.PasswordUtils;
import com.smarthealth.common.constant.HttpCodeConstant;
import com.smarthealth.common.context.BaseContext;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.DTO.UserDTO;
import com.smarthealth.domain.Entity.User;
import com.smarthealth.domain.Entity.UserInfo;
import com.smarthealth.domain.DTO.HealthReportDTO;
import com.smarthealth.domain.VO.UserInfoVO;
import com.smarthealth.service.HealthAdviceService;
import com.smarthealth.service.HealthReportService;
import com.smarthealth.service.UserInfoService;
import com.smarthealth.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/sht/user")
public class UserController {

    @Resource
    private AliOssUtil aliOss;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private HealthReportService healthReportService;

    @Resource
    private HealthAdviceService healthAdviceService;


    //手机号、密码登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return userService.loginByPhone(userDTO);
    }


    //手机号、密码注册账户
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        if(userDTO.getPhone().length() != 11){
            return Result.error(HttpCodeConstant.CONFLICT,"手机号格式错误,请重新尝试");
        }

        User user = userService.getUserByPhone(userDTO.getPhone());
        if(user != null){
            return Result.error(HttpCodeConstant.CONFLICT,"该手机号已注册过账户");
        }
        if(userDTO.getPassword() == null || userDTO.getRe_password() == null ||!userDTO.getPassword().equals(userDTO.getRe_password())){
            return Result.error(HttpCodeConstant.CONFLICT,"未正确输入两次密码,请重试");
        }
        user = User.builder()
                .phone(userDTO.getPhone())
                .password(PasswordUtils.md5WithSalt(userDTO.getPassword()))
                .updatedTime(LocalDateTime.now())
                .createdTime(LocalDateTime.now())
                .build();

        if(userService.save(user)){
            return Result.ok();
        }
        return Result.error("出现异常,注册失败");
    }


    //用户添加或修改个人信息
    @PostMapping("/info")
    public Result setUserInfo(@RequestBody UserInfo userInfo){
        Long userId = BaseContext.getCurrentId();

        userInfo.setUserId(userId);
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdatedTime(LocalDateTime.now());
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserId, userId).eq(UserInfo::getIsDeleted, 0));
        if(one != null){
            userInfo.setId(one.getId());
            if(userInfoService.saveOrUpdate(userInfo)){
                return Result.ok();
            }
        }
        if(userInfoService.saveOrUpdate(userInfo)){
            return Result.ok();
        }
        return Result.error("出现异常,设置个人信息失败");
    }


    //获取用户信息
    @GetMapping("/info")
    public Result getUserInfoById(){
        //获取当前请求的用户id
        Long userId = BaseContext.getCurrentId();
        UserInfo userInfo = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserId, userId).eq(UserInfo::getIsDeleted, 0));
        UserInfoVO userInfoVO = new UserInfoVO();
        if(userInfo == null){
            return Result.error("用户信息不存在");
        }
        BeanUtils.copyProperties(userInfo,userInfoVO);
        return Result.ok(userInfoVO);
    }


    //上传 头像/体检报告
    @PostMapping("/file")
    public Result uploadAvatar(MultipartFile file) throws Exception{
        //获取文件的原始名字
        String originalFilename = file.getOriginalFilename();

        //保证文件名市唯一的，防止文件覆盖
        String url = aliOss.uploadFile(UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf(".")), file.getInputStream());
        return Result.ok(url);
    }


    //识别存储体检报告，并给出建议，然后将建议存到mysql中
    @PostMapping("/healthReportUpload")
    public Result uploadHealthReport(@RequestBody HealthReportDTO healthReportDTO){
        System.out.println(healthReportDTO.getFilePath());
        Long userId = BaseContext.getCurrentId();
        return healthReportService.recognize(healthReportDTO.getFilePath(), userId);
    }


    //获取体检报告信息
    @GetMapping("/healthReport")
    public Result getHealthReport(){
        Long userId = BaseContext.getCurrentId();
        return healthReportService.getNewReport(userId);
    }


    //获取体检报告建议
    @GetMapping("/healthReport/suggestion")
    public Result getHealthReportSuggestion(){
        Long userId = BaseContext.getCurrentId();
        return healthAdviceService.getHealthReportBodyAdvice(userId);
    }



}
