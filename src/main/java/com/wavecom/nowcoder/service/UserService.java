package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.vo.RegisterVO;

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
}
