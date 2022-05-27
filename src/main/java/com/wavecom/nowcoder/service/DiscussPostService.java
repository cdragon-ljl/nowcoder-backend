package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讨论帖信息表 服务类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
public interface DiscussPostService extends IService<DiscussPost> {

    /**
     * 新增一条帖子
     * @param discussPost
     * @return
     */
    public int addDiscussPost(DiscussPost discussPost);

    /**
     * 获取帖子详情
     * @param discussPostId
     * @return
     */
    public DiscussPost getDiscussPostById(int discussPostId);

    /**
     * 更新评论数量
     * @param entityId
     * @param count
     * @return
     */
    public boolean updateCommentCount(Integer entityId, Integer count);
}
