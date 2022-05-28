package com.wavecom.nowcoder.controller;


import com.wavecom.nowcoder.annotation.LoginRequired;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.LikeService;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.utils.HostUtil;
import com.wavecom.nowcoder.utils.NowCoderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Api
@Slf4j
@RestController
@RequestMapping("/nowcoder/user")
public class UserController {

    @Value("${nowcoder.path.domain}")
    private String domain;

    @Value("${nowcoder.path.upload.header}")
    private String headerPath;

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("上传头像")
    @LoginRequired
    @PostMapping("/setting")
    public Result uploadHeader(MultipartFile headerImage) {
        if (headerImage == null) {
            return Result.error("请选择要上传的头像");
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return Result.error("文件格式错误");
        }
        //生成随机文件名
        filename = NowCoderUtil.generateUUID() + suffix;
        String projectPath = System.getProperty("user.dir");
        String headerUrl = projectPath  + headerPath + "/" + filename;
        File dest = new File(headerUrl);
        try {
            //上传文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
            throw new RuntimeException("文件上传失败");
        }
        User user = hostUtil.getUser();
        boolean update = userService.updateHeaderById(user.getId(), headerUrl);

        return update ? Result.ok("头像上传成功") : Result.error("头像上传失败");
    }

    @ApiOperation("查看头像")
    @LoginRequired
    @GetMapping("/header/{id}")
    public void getHeader(@PathVariable("id") int id, HttpServletResponse response) {
        String filename = userService.selectHeaderById(id);
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try (
                FileInputStream inputStream = new FileInputStream(filename);
                ServletOutputStream outputStream = response.getOutputStream();
                ) {
            byte[] bytes = new byte[1024];
            int b = 0;
            while ((b = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, b);
            }
        } catch (IOException e) {
            log.error("读取头像失败");
        }
    }

    @ApiOperation("个人主页")
    @GetMapping("/profile/{uid}")
    public Result<User> getProfile(@PathVariable("uid") int uid) {
        User user = userService.selectById(uid);
        if (user == null) {
            return Result.error(404, "用户不存在", null);
        }
        Long userLikeCount = likeService.getUserLikeCount(uid);

        return Result.ok(user);
    }
}

