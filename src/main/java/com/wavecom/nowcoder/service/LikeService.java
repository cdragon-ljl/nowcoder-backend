package com.wavecom.nowcoder.service;


/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LikeService
 * @Date 2022/5/27 4:51 PM
 **/
public interface LikeService {
    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @param entityUserId
     */
    public void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 获取点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    public Long getEntityLikeCount(int entityType, int entityId);

    /**
     * 查看点赞状态
     * @param id
     * @param entityType
     * @param entityId
     * @return
     */
    public Integer getEntityLikeStatus(Integer id, int entityType, int entityId);

    /**
     * 获取某个用户获得的赞
     * @param id
     * @return
     */
    public Long getUserLikeCount(Integer id);
}
