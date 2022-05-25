package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.LoginTicket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录记录表 服务类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
public interface LoginTicketService extends IService<LoginTicket> {

    /**
     * 根据ticket查找登录记录
     * @param ticket
     * @return
     */
    public LoginTicket selectByTicket(String ticket);
}
