package com.smarthealth.common.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthealth.common.Utils.JwtUtils;
import com.smarthealth.common.context.BaseContext;
import com.smarthealth.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 在请求处理之前进行拦截处理
     *
     * @param request  请求对象，用于获取请求头中的Authorization信息
     * @param response 响应对象，用于发送错误信息
     * @param handler  处理请求的对象，本方法中未使用
     * @return 返回true继续执行下一个拦截器或请求处理方法，返回false则中断执行
     * @throws Exception 抛出异常时，中断执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("拦截器触发");
        // 获取请求头中的 Authorization 信息
        String token = request.getHeader("Authorization");
        System.out.println(token);

        // 检查 token 是否存在
        if (token == null) {
            log.warn("请求未携带有效的 Token,拦截！");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token is missing");
            return false;
        }

        // 验证 token 是否有效
        if (!JwtUtils.validateToken(token)) {
            log.warn("请求携带的 Token 无效或已过期,拦截！");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token is invalid or expired");
            return false;
        }

        Number userId = (Number)JwtUtils.getClaim(token, "userid");
        BaseContext.setCurrentId(userId.longValue());

        // 如果 token 有效，继续执行请求
        return true;
    }
}
