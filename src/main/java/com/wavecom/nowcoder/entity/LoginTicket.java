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
 * 登录记录表
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LoginTicket对象", description="登录记录表")
public class LoginTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String ticket;

    @ApiModelProperty(value = "0-有效; 1-无效;")
    private Integer status;

    private Date expired;


}
