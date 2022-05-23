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
 * 讨论帖信息表
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DiscussPost对象", description="讨论帖信息表")
public class DiscussPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private String title;

    private String content;

    @ApiModelProperty(value = "0-普通; 1-置顶;")
    private Integer type;

    @ApiModelProperty(value = "0-正常; 1-精华; 2-拉黑;")
    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private Double score;


}
