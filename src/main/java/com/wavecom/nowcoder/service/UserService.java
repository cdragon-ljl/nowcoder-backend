package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.LoginTicket;
import com.wavecom.nowcoder.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.vo.LoginVO;
import com.wavecom.nowcoder.vo.RegisterVO;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册方法
     * @param registerVO
     * @return
     */
    public Result register(RegisterVO registerVO);

    /**
     * 激活账号
     * @param id
     * @param code
     * @return
     */
    public Result activation(int id, String code);

    /**
     * 登录账号
     * @param loginVO
     * @param expiration
     * @return
     */
    public Map<String, Object> login(LoginVO loginVO, long expiration);

    /**
     * 退出登录
     * @param ticket
     */
    public void logout(String ticket);

    /**
     * 根据用户id查找
     * @param id
     * @return
     */
    public User selectById(Integer id);

    /**
     * 根据用户id更新头像
     * @param id
     * @param headerUrl
     * @return
     */
    public boolean updateHeaderById(Integer id, String headerUrl);

    /**
     * 根据用户id查找头像
     * @param id
     * @return
     */
    public String selectHeaderById(Integer id);
}
