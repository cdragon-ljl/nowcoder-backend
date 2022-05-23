package com.wavecom.nowcoder.controller;

import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LoginController
 * @Date 2022/5/23 3:41 下午
 **/
@Api
@RestController
@RequestMapping("/nowcoder")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation("register")
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterVO registerVO) {
        return userService.register(registerVO);
    }

    @ApiOperation("activation")
    @GetMapping("/activation/{id}/{code}")
    public Result activation(@PathVariable("id") int id, @PathVariable("code") String code) {
        return userService.activation(id, code);
    }
}
