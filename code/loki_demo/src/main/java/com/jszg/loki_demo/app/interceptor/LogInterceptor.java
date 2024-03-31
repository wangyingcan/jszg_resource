package com.jszg.loki_demo.app.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 1. springboot3.X版本对应的依赖变成了如下的样式
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

// 2.此类是用于创建日志请求相关的拦截器，下一步需要注册此拦截器
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private final static String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        String remoteIp = request.getRemoteAddr();
        String uuid = UUID.randomUUID().toString();
        log.info("put requestId ({}) to logger", uuid);
        log.info("request id:{}, client ip:{}, X-Forwarded-For:{}", uuid, remoteIp, xForwardedForHeader);
        MDC.put(REQUEST_ID, uuid);
        MDC.put("remoteIp", remoteIp);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String uuid = MDC.get(REQUEST_ID);
        log.info("remove requestId ({}) from logger", uuid);
        MDC.remove(REQUEST_ID);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
