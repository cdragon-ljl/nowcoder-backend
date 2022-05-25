package com.wavecom.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wavecom.nowcoder.entity.LoginTicket;
import com.wavecom.nowcoder.mapper.LoginTicketMapper;
import com.wavecom.nowcoder.service.LoginTicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录记录表 服务实现类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Service
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket> implements LoginTicketService {

    @Override
    public LoginTicket selectByTicket(String ticket) {
        return baseMapper.selectOne(new QueryWrapper<LoginTicket>().eq("ticket", ticket));
    }
}
