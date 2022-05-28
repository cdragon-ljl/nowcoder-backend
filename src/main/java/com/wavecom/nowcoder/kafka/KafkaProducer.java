package com.wavecom.nowcoder.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wavecom.nowcoder.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File KafkaProducer
 * @Date 2022/5/28 3:15 PM
 **/
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
