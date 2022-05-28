package com.wavecom.nowcoder.service;

import java.util.List;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File FollowService
 * @Date 2022/5/27 5:19 PM
 **/
public interface FollowService {
    /**
     * 关注实体
     * @param id
     * @param entityType
     * @param entityId
     */
    public void follow(Integer id, int entityType, int entityId);

    /**
     * 取消关注
     * @param id
     * @param entityType
     * @param entityId
     */
    public void unfollow(Integer id, int entityType, int entityId);

    /**
     * 查询用户关注的人
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findFollowees(int userId);

    /**
     * 查询是否已关注
     * @param id
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean hasFollowed(Integer id, int entityType, int entityId);

    /**
     * 查询用户的粉丝
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findFollowers(int userId);
}
