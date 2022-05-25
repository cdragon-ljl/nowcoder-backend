package com.wavecom.nowcoder.utils;

import com.wavecom.nowcoder.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File HostUtil
 * @Date 2022/5/24 5:20 下午
 **/
@Component
public class HostUtil {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
