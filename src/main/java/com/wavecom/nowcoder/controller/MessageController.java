package com.wavecom.nowcoder.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wavecom.nowcoder.constant.NowCoderConstant;
import com.wavecom.nowcoder.entity.Message;
import com.wavecom.nowcoder.entity.User;
import com.wavecom.nowcoder.result.Result;
import com.wavecom.nowcoder.service.MessageService;
import com.wavecom.nowcoder.service.UserService;
import com.wavecom.nowcoder.utils.HostUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author cdragon-ljl
 * @since 2022-05-23
 */
@Api
@RestController
@RequestMapping("/nowcoder/message")
public class MessageController {

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @ApiOperation("获取私信列表")
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getMessageList(long cur, long size) {
        User user = hostUtil.getUser();
        Page<Message> messagePage = new Page<>();
        messagePage.setCurrent(cur);
        messagePage.setMaxLimit(size);

        List<Message> messageList = messageService.selectMessageList(user.getId());
        List<Map<String, Object>> messageMap = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("messages", message);
                map.put("messageCount", messageService.selectMessageCount(message.getConversationId()));
                map.put("unreadCount", messageService.selectUnreadMessageCount(user.getId(), message.getConversationId()));
                int targetId = user.getId().equals(message.getFromId()) ? message.getFromId() : message.getToId();
                map.put("targetId", userService.selectById(targetId));

                messageMap.add(map);
            }
        }
        int unreadMessageCount = messageService.selectUnreadMessageCount(user.getId(), null);
        int unreadNoticeCount = messageService.selectNoticeUnreadCount(user.getId(), null);

        return Result.ok(messageMap);
    }

    @ApiOperation("通知列表")
    @GetMapping("/notice/list")
    public Result<List<Message>> getNoticeList() {
        User user = hostUtil.getUser();
        Message message = messageService.getLatesNotice(user.getId(), NowCoderConstant.TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);
        }
        return Result.ok(null);
    }

    @ApiOperation("查看私信详情")
    @GetMapping("/detail/{conversationId}")
    public Result<List<Message>> getMessagesDetail(@PathVariable("conversationId") String conversationId) {
        List<Message> messageList = messageService.selectMessagesByConversationId(conversationId);
        List<Message> messages = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                if (hostUtil.getUser().getId().equals(message.getToId())
                        && message.getStatus() != NowCoderConstant.MESSAGE_STATUS_UNREAD) {
                    messages.add(message);
                }
            }
        }
        if (messages != null) {
            messageService.readMessageById(messages);
        }

        return Result.ok(messageList);
    }

    @ApiOperation("发送私信")
    @PostMapping("/send")
    public Result sendMessage(String to, String content) {
        User target = userService.selectByUsername(to);
        if (target == null) {
            return Result.error("目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostUtil.getUser().getId());
        message.setToId(target.getId());
        message.setConversationId(message.getFromId() + "->" + message.getToId());
        message.setContent(content);
        message.setCreateTime(new Date());
        int insert = messageService.addMessage(message);

        return insert == 1 ? Result.ok("发送成功") : Result.error("发送失败");
    }
}

