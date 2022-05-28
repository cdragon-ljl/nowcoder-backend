package com.wavecom.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Comment;
import com.wavecom.nowcoder.mapper.CommentMapper;
import com.wavecom.nowcoder.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wavecom.nowcoder.service.DiscussPostService;
import com.wavecom.nowcoder.utils.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Autowired
    private DiscussPostService discussPostService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addComment(Comment comment) {
        comment.setContent(sensitiveUtil.filter(comment.getContent()));
        int insert = baseMapper.insert(comment);

        if (comment.getEntityType() == NowCoderConstant.ENTITY_TYPE_POST) {
            Integer count = baseMapper.selectCount(new QueryWrapper<Comment>()
                    .eq("status", 0)
                    .eq("entity_type", comment.getEntityType())
                    .eq("entity_id", comment.getEntityId()));
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return insert;
    }

    @Override
    public List<Comment> selectComment(int discussPostId) {
        List<Comment> comments = baseMapper.selectList(new QueryWrapper<Comment>()
                .eq("entity_id", discussPostId)
                .eq("entity_type", NowCoderConstant.ENTITY_TYPE_POST)
                .eq("status", 0));
        return comments;
    }

    @Override
    public Comment getCommentById(Integer entityId) {
        return baseMapper.selectById(entityId);
    }
}
