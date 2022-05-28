package com.wavecom.nowcoder.controller;

import com.google.code.kaptcha.Producer;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.utils.NowCoderUtil;
import com.wavecom.nowcoder.utils.RedisUtil;
import com.wavecom.nowcoder.vo.LoginVO;
import com.wavecom.nowcoder.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LoginController
 * @Date 2022/5/23 3:41 下午
 **/
@Api
@Slf4j
@RestController
@RequestMapping("/nowcoder")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @ApiOperation("首页")
    @GetMapping({"/", "/index"})
    public Result index() {
        return Result.ok("欢迎访问");
    }

    @ApiOperation("注册账号")
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterVO registerVO) {
        return userService.register(registerVO);
    }

    @ApiOperation("激活账号")
    @GetMapping("/activation/{id}/{code}")
    public Result activation(@PathVariable("id") int id, @PathVariable("code") String code) {
        return userService.activation(id, code);
    }

    @ApiOperation("获取验证码")
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码文本及图片
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

//        //将验证码存入Session
//        session.setAttribute("kaptcha", text);

        //使用Redis重构
        String kaptchaOwner = NowCoderUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //将验证码存入Redis，过期时间60s
        String kaptchaKey = RedisUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey, text, 60, TimeUnit.SECONDS);

        //将图片输出给浏览器
        response.setContentType("image/png");

        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            log.error("响应验证码失败：" + e.getMessage());
        }
    }

    @ApiOperation("登陆")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginVO loginVO,
                        HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
        //检查验证码
//        String kaptcha = (String) session.getAttribute("kaptcha");

        //使用Redis重构
        String kaptcha = null;
        if (StringUtils.isBlank(kaptchaOwner)) {
            String kaptchaKey = RedisUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(kaptchaKey);
        }
        if (!kaptcha.equals(loginVO.getCode())) {
            return Result.error("验证码错误");
        }
        long expiration = loginVO.getRemerberMe() ? NowCoderConstant.REMEMBER_EXPIRED_SECONDS
                : NowCoderConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(loginVO, expiration);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge((int) expiration);

            response.addCookie(cookie);
            return Result.ok(map.get("message").toString());
        }
        return Result.error(map.get("message").toString());
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return Result.ok("退出成功");
    }
}
