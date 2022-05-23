package com.wavecom.nowcoder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String email;

    @ApiModelProperty(value = "0-普通用户; 1-超级管理员; 2-版主;")
    private Integer type;

    @ApiModelProperty(value = "0-未激活; 1-已激活;")
    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;


}
