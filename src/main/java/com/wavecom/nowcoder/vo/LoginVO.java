package com.wavecom.nowcoder.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File LoginVO
 * @Date 2022/5/24 3:26 下午
 **/
@Data
public class LoginVO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String code;
    private Boolean remerberMe;
}
