package com.wavecom.nowcoder.controller;

import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.LikeService;
import com.wavecom.nowcoder.utils.HostUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LikeController
 * @Date 2022/5/27 5:01 PM
 **/
@Api
@RestController
@RequestMapping("/nowcoder")
public class LikeController {

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private LikeService likeService;

    @ApiOperation("点赞")
    @PostMapping("/like")
    public Result<Map<String, Object>> like(int entityType, int entityId, int entityUserId) {
        User user = hostUtil.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        long likeCount = likeService.getEntityLikeCount(entityType, entityId);
        int likeStatus = likeService.getEntityLikeStatus(user.getId(), entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return Result.ok("点赞成功", map);
    }


}
