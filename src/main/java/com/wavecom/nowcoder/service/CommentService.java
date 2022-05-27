package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
public interface CommentService extends IService<Comment> {

    /**
     * 发表评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment);

    /**
     * 查看评论
     * @param discussPostId
     * @return
     */
    public List<Comment> selectComment(int discussPostId);
}
