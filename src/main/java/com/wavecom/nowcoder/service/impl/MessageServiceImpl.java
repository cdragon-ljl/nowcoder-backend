package com.wavecom.nowcoder.service.impl;

import com.wavecom.nowcoder.entity.Message;
import com.wavecom.nowcoder.mapper.MessageMapper;
import com.wavecom.nowcoder.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
