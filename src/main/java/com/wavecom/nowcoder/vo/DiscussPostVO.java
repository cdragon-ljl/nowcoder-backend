package com.wavecom.nowcoder.vo;

import com.wavecom.nowcoder.entity.DiscussPost;
import com.wavecom.nowcoder.entity.User;
import lombok.Data;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File DiscussPostVO
 * @Date 2022/5/25 10:35 下午
 **/
@Data
public class DiscussPostVO {
    private String username;

    private DiscussPost discussPost;
}
