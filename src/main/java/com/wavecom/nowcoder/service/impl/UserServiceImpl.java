package com.wavecom.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wavecom.nowcoder.entity.LoginTicket;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.mapper.UserMapper;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.LoginTicketService;
import com.wavecom.nowcoder.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wavecom.nowcoder.utils.MailUtil;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.utils.NowCoderUtil;
import com.wavecom.nowcoder.utils.RedisUtil;
import com.wavecom.nowcoder.vo.LoginVO;
import com.wavecom.nowcoder.vo.RegisterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private MailUtil mailUtil;

    @Value("${nowcoder.path.domain}")
    private String domain;

    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result register(RegisterVO registerVO) {
        User user = new User();
        BeanUtils.copyProperties(registerVO, user);
        //判断用户名是否存在
        User one = baseMapper.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (one != null) {
            return Result.error("用户名已存在");
        }
        //判断邮箱是否存在
        one = baseMapper.selectOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        if (one != null) {
            return Result.error("邮箱已存在");
        }
        //开始注册
        user.setSalt(NowCoderUtil.generateUUID().substring(0, 6));
        user.setPassword(NowCoderUtil.md5(user.getPassword() + user.getSalt()));
        //普通用户
        user.setType(NowCoderConstant.USER_TYPE_NORMAL);
        //未激活
        user.setStatus(NowCoderConstant.ACTIVATION_NOT);
        //生成激活码
        user.setActivationCode(NowCoderUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.wavecom.nowcoder.com/head/%dt.png",
                new Random().nextInt(100)));
        user.setCreateTime(new Date());

        baseMapper.insert(user);

        //--------------激活邮件-----------------
        String url = domain  + "/activation/" +
                user.getId() + "/" + user.getActivationCode();
        String content = "Click to Activation : " + url;
        mailUtil.sendMail(user.getEmail(), "激活账号", content);

        return Result.ok("注册成功");
    }

    @Override
    public Result activation(int id, String code) {
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        if (user.getStatus() == NowCoderConstant.ACTIVATION_REPEAT) {
            return Result.error("重复激活");
        }
        if (user.getActivationCode().equals(code)) {
            baseMapper.update(user, new UpdateWrapper<User>().eq("id", id)
                    .set("status", NowCoderConstant.ACTIVATION_SUCCESS));
            return Result.ok("激活成功");
        } else {
            return Result.error("激活失败");
        }
    }

    @Override
    public Map<String, Object> login(LoginVO loginVO, long expiration) {
        Map<String, Object> map = new HashMap<>();
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", loginVO.getUsername()));
        if (user == null) {
            map.put("message", "用户不存在");
            return map;
        }
        if (user.getStatus() != NowCoderConstant.ACTIVATION_SUCCESS) {
            map.put("message", "用户未激活");
            return map;
        }
        String password = NowCoderUtil.md5(loginVO.getPassword() + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("message", "密码错误");
            return map;
        }
        //生成登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(NowCoderUtil.generateUUID());
        loginTicket.setStatus(NowCoderConstant.LOGIN_TICKET_VALID);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiration));

//        loginTicketService.save(loginTicket);
        //使用Redis重构
        String ticketKey = RedisUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, loginTicket);

        map.put("message", "登录成功");
        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    @Override
    public void logout(String ticket) {
//        loginTicketService.update(new UpdateWrapper<LoginTicket>().eq("ticket", ticket).set("status", NowCoderConstant.LOGIN_TICKET_INVALID));
        String ticketKey = RedisUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(NowCoderConstant.LOGIN_TICKET_INVALID);
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
    }

    @Override
    public User selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean updateHeaderById(Integer id, String headerUrl) {
        return this.update(new UpdateWrapper<User>().eq("id", id).set("header_url", headerUrl));
    }

    @Override
    public String selectHeaderById(Integer id) {
        return baseMapper.selectById(id).getHeaderUrl();
    }

    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }
}
