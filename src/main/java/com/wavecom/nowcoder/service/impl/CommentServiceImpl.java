package com.wavecom.nowcoder.service.impl;

import com.wavecom.nowcoder.entity.Comment;
import com.wavecom.nowcoder.mapper.CommentMapper;
import com.wavecom.nowcoder.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
