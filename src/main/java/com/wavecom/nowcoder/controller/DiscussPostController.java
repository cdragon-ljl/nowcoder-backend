package com.wavecom.nowcoder.controller;


import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.DiscussPost;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.DiscussPostService;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.utils.HostUtil;
import com.wavecom.nowcoder.vo.DiscussPostVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 讨论帖信息表 前端控制器
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Api
@RestController
@RequestMapping("/nowcoder/discuss")
public class DiscussPostController {

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @ApiOperation("发布帖子")
    @PostMapping("/add")
    public Result addDiscussPost(String title, String content) {
        User user = hostUtil.getUser();
        if (user == null) {
            return Result.error("请登录后发布");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId().toString());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setType(NowCoderConstant.DISCUSS_POST_TYPE_NORMAL);
        discussPost.setCreateTime(new Date());

        int insert = discussPostService.addDiscussPost(discussPost);

        return insert == 1 ? Result.ok("发布成功") : Result.error("发布失败");
    }

    @ApiOperation("帖子详情")
    @GetMapping("/detail/{discussPostId}")
    public Result<DiscussPostVO> getDiscussPost(@PathVariable("discussPostId") int discussPostId) {
        DiscussPostVO discussPostVO = new DiscussPostVO();
        DiscussPost discussPost = discussPostService.getDiscussPostById(discussPostId);
        User user = userService.selectById(Integer.parseInt(discussPost.getUserId()));
        discussPostVO.setUsername(user.getUsername());
        discussPostVO.setDiscussPost(discussPost);

        return Result.ok(discussPostVO);
    }
}

