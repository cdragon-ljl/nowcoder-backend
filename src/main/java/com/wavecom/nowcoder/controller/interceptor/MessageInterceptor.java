package com.wavecom.nowcoder.controller.interceptor;

import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.service.MessageService;
import com.wavecom.nowcoder.utils.HostUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File MessageInterceptor
 * @Date 2022/5/28 4:45 PM
 **/
@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostUtil.getUser();
        if (user != null) {

        }
    }
}
