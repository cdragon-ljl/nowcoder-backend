package com.wavecom.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wavecom.nowcoder.entity.DiscussPost;
import com.wavecom.nowcoder.mapper.DiscussPostMapper;
import com.wavecom.nowcoder.service.DiscussPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wavecom.nowcoder.utils.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讨论帖信息表 服务实现类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService {

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        discussPost.setTitle(sensitiveUtil.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveUtil.filter(discussPost.getContent()));

        return baseMapper.insert(discussPost);
    }

    @Override
    public DiscussPost getDiscussPostById(int discussPostId) {
        return baseMapper.selectById(discussPostId);
    }

    @Override
    public boolean updateCommentCount(Integer entityId, Integer count) {
        return this.update(new UpdateWrapper<DiscussPost>().eq("id", entityId).set("comment_count", count));
    }
}
