package com.wavecom.nowcoder.service.impl;

import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.mapper.UserMapper;
import com.wavecom.nowcoder.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
