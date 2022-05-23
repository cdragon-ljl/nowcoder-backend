package com.wavecom.nowcoder.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File RegisterVO
 * @Date 2022/5/23 3:34 下午
 **/
@Data
public class RegisterVO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(message = "密码长度不规范", min = 4, max = 10)
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
}
