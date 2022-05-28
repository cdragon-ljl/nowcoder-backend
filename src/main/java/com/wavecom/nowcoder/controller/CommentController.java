package com.wavecom.nowcoder.controller;


import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Comment;
import com.wavecom.nowcoder.entity.DiscussPost;
import com.wavecom.nowcoder.entity.Event;
import com.wavecom.nowcoder.kafka.KafkaProducer;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.CommentService;
import com.wavecom.nowcoder.service.DiscussPostService;
import com.wavecom.nowcoder.utils.HostUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Api
@RestController
@RequestMapping("/nowcoder/comment")
public class CommentController {

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @ApiOperation("查询评论")
    @GetMapping("/get/{discussPostId}")
    public Result<List<Comment>> getComment(@PathVariable("discussPostId") int discussPostId) {
        List<Comment> comments = commentService.selectComment(discussPostId);
        return Result.ok(comments);
    }

    @ApiOperation("评论帖子")
    @PostMapping("/add/{discussPostId}")
    public Result addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostUtil.getUser().getId());
        comment.setEntityType(NowCoderConstant.ENTITY_TYPE_POST);
        comment.setEntityId(discussPostId);
        comment.setStatus(NowCoderConstant.COMMENT_TYPE_NORMAL);
        comment.setCreateTime(new Date());
        int add = commentService.addComment(comment);

        Event event = new Event();
        event.setTopic(NowCoderConstant.TOPIC_COMMENT);
        event.setId(hostUtil.getUser().getId());
        event.setEntityType(comment.getEntityType());
        event.setEntityId(comment.getEntityId());
        event.setData("postId", discussPostId);
        if (comment.getEntityType() == NowCoderConstant.ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.getDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getId());
        } else if (comment.getEntityType() == NowCoderConstant.ENTITY_TYPE_COMMENT) {
            Comment target = commentService.getCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        kafkaProducer.fireEvent(event);
        return add == 1 ? Result.ok("评论成功") : Result.error("评论失败");
    }

}

