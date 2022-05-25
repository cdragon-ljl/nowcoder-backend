package com.wavecom.nowcoder.controller.interceptor;

import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.LoginTicket;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.service.LoginTicketService;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.utils.CookieUtil;
import com.wavecom.nowcoder.utils.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LoginTicketInterceptor
 * @Date 2022/5/24 5:19 下午
 **/
@Slf4j
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private HostUtil hostUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketService.selectByTicket(ticket);
            if (loginTicket != null
                    && loginTicket.getStatus() == NowCoderConstant.LOGIN_TICKET_VALID
                    && loginTicket.getExpired().after(new Date())) {
                User user = userService.selectById(loginTicket.getUserId());
                hostUtil.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostUtil.getUser();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostUtil.clear();
    }
}
