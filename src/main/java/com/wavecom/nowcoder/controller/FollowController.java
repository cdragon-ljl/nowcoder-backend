package com.wavecom.nowcoder.controller;

import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Event;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.kafka.KafkaProducer;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.FollowService;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.utils.HostUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File FollowController
 * @Date 2022/5/28 2:12 PM
 **/
@Api
@RestController
@RequestMapping("/nowcoder")
public class FollowController {

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @ApiOperation("关注")
    @PostMapping("/follow")
    public Result follow(int entityType, int entityId) {
        User user = hostUtil.getUser();
        followService.follow(user.getId(), entityType, entityId);

        Event event = new Event();
        event.setTopic(NowCoderConstant.TOPIC_FOLLOW);
        event.setId(user.getId());
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setEntityUserId(entityId);
        kafkaProducer.fireEvent(event);

        return Result.ok("已关注");
    }

    @ApiOperation("取消关注")
    @PostMapping("/unfollow")
    public Result unfollow(int entityType, int entityId) {
        User user = hostUtil.getUser();
        followService.unfollow(user.getId(), entityType, entityId);

        return Result.ok("已取消关注");
    }

    @ApiOperation("查看关注的人")
    @GetMapping("/followees/{userId}")
    public Result<List<Map<String, Object>>> getFollowees(@PathVariable("userId") int userId) {
        if (hostUtil.getUser() == null) {
            return Result.error();
        }
        User user = userService.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在");
        }
        List<Map<String, Object>> userList = followService.findFollowees(userId);
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");

                boolean hasFollowed = followService.hasFollowed(hostUtil.getUser().getId(), NowCoderConstant.ENTITY_TYPE_USER, userId);
                map.put("hasFollowed", hasFollowed);
            }
        }
        return Result.ok(userList);
    }

    @ApiOperation("查看某用户的粉丝")
    @GetMapping("/followers/{userId}")
    public Result<List<Map<String, Object>>> getFollowers(@PathVariable("userId") int userId) {
        User user = userService.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在");
        }
        List<Map<String, Object>> userList = followService.findFollowers(userId);
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");

                boolean hasFollowed = followService.hasFollowed(hostUtil.getUser().getId(), NowCoderConstant.ENTITY_TYPE_USER, userId);
                map.put("hasFollowed", hasFollowed);
            }
        }
        return Result.ok(userList);
    }
}
