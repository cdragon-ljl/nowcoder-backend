package com.wavecom.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Message;
import com.wavecom.nowcoder.mapper.MessageMapper;
import com.wavecom.nowcoder.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wavecom.nowcoder.utils.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Override
    public List<Message> selectMessageList(Integer id) {
        return baseMapper.selectList(new QueryWrapper<Message>()
                .ne("status", 2)
                .and(w -> {
                    w.eq("from_id", id).or().eq("to_id", id);
                }));
    }

    @Override
    public Integer selectMessageCount(String conversationId) {
        return baseMapper.selectCount(new QueryWrapper<Message>()
                .eq("conversation_id", conversationId));
    }

    @Override
    public Integer selectUnreadMessageCount(Integer id, String conversationId) {
        return baseMapper.selectCount(new QueryWrapper<Message>()
                .eq("to_id", id)
                .eq("conversation_id", conversationId)
                .eq("status", NowCoderConstant.MESSAGE_STATUS_UNREAD));
    }

    @Override
    public List<Message> selectMessagesByConversationId(String conversationId) {
        return baseMapper.selectList(new QueryWrapper<Message>().eq("conversation_id", conversationId));
    }

    @Override
    public Integer addMessage(Message message) {
        message.setContent(sensitiveUtil.filter(message.getContent()));

        return baseMapper.insert(message);
    }

    @Override
    public void readMessageById(List<Message> messages) {
        this.updateBatchById(messages);
    }
}
