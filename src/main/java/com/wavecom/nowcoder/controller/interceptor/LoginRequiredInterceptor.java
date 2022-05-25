package com.wavecom.nowcoder.controller.interceptor;

import com.wavecom.nowcoder.annotation.LoginRequired;
import com.wavecom.nowcoder.utils.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LoginRequiredInterceptor
 * @Date 2022/5/25 4:30 下午
 **/
@Slf4j
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostUtil hostUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //保证拦截的是方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (hostUtil.getUser() == null && loginRequired != null) {
                log.error("用户未登录");
                return false;
            }
        }
        return true;
    }
}
