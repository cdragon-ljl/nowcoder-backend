package com.wavecom.nowcoder.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File Event
 * @Date 2022/5/28 3:23 PM
 **/
@Data
public class Event {
    private String topic;
    private Integer id;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public void setData(String key, Object vale) {
        this.data.put(key, vale);
    }
}
