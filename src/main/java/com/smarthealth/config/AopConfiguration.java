package com.smarthealth.config;




import com.smarthealth.service.LogService;
import com.smarthealth.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.Resource;

/**
 * @author wyhhh
 */
@Configuration
@Aspect
@EnableAspectJAutoProxy
public class AopConfiguration {
    @Resource
    private LogService loginLogService;

    @Resource
    private UserService userService;

    /**
     * 环绕通知，用于在执行带有@AddLoginLogAnnotation注解的方法前后添加登录日志
     * 该方法主要记录用户登录信息，包括登录成功或失败的情况
     *
     * @param joinPoint 切入点对象，提供了关于当前执行方法的信息
     * @return 执行链中下一个执行的方法的结果
     * @throws Throwable 如果执行过程中发生异常
     */
//    @Around("@annotation(com.smarthealth.common.annotation.AddLoginLogAnnotation)")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 获取方法参数
//        Object[] args = joinPoint.getArgs();
//        UserDTO userLoginDTO = (UserDTO) args[0];
//
//        // 执行目标方法
//        Result result= (Result) joinPoint.proceed();
//
//        //添加失败登录日志
//        if(result.getCode()==500){
//            loginLogService.addLoginLog(LoginLog.builder()
//                    .userId(userLoginDTO.getUsername())
//                    .msg(result.getMessage())
//                    .status("登录失败")
//                    .loginTime(LocalDateTime.now()).build());
//        }
//
//        // 根据执行结果记录登录日志
//        if (result.getCode() == 200) {
//            loginLogService.addLoginLog(LoginLogEntity.builder()
//                    .id(null)
//                    .username(userLoginDTO.getUsername())
//                    .msg("登录成功")
//                    .status("登录成功")
//                    .loginTime(LocalDateTime.now())
//                    .build());
//        }
//        // 返回目标方法的结果给原方法
//        return result;
//    }
}