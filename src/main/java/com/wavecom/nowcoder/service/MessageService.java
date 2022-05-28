package com.wavecom.nowcoder.service;

import com.wavecom.nowcoder.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
public interface MessageService extends IService<Message> {

    /**
     * 根据用户id获取私信列表
     * @param id
     * @return
     */
    public List<Message> selectMessageList(Integer id);

    /**
     * 根据会话id查询私信数量
     * @param conversationId
     * @return
     */
    public Integer selectMessageCount(String conversationId);

    /**
     * 根据用户id和会话id查询未读私信数量
     * @param id
     * @param conversationId
     * @return
     */
    public Integer selectUnreadMessageCount(Integer id, String conversationId);

    /**
     * 根据回话id查询私信详情
     * @param conversationId
     * @return
     */
    public List<Message> selectMessagesByConversationId(String conversationId);

    /**
     * 发送私信
     * @param message
     * @return
     */
    public Integer addMessage(Message message);

    /**
     * 修改私信状态为已读
     * @param messages
     */
    public void readMessageById(List<Message> messages);

    /**
     * 查询主题下最新的通知
     * @param id
     * @param topic
     * @return
     */
    public Message getLatesNotice(Integer id, String topic);

    /**
     * 查询主题所包含的通知数量
     * @param id
     * @param topic
     * @return
     */
    public int selectNoticeCount(Integer id, String topic);

    /**
     * 查询未读的通知数量
     * @param id
     * @param topic
     * @return
     */
    public int selectNoticeUnreadCount(Integer id, String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param id
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> selectNotices(Integer id, String topic, int offset, int limit);
}
