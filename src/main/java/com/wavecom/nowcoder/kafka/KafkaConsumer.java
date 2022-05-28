package com.wavecom.nowcoder.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Event;
import com.wavecom.nowcoder.entity.Message;
import com.wavecom.nowcoder.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File KafkaConsumer
 * @Date 2022/5/28 3:14 PM
 **/
@Slf4j
@Component
public class KafkaConsumer {
    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {"test"})
    public void handlerMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }

    @KafkaListener(topics = {NowCoderConstant.TOPIC_COMMENT, NowCoderConstant.TOPIC_LIKE, NowCoderConstant.TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return;
        }
        //发送站内通知
        Message message = new Message();
        message.setFromId(NowCoderConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
