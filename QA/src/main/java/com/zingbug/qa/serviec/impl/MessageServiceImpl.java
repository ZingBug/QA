package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.MessageDao;
import com.zingbug.qa.pojo.Message;
import com.zingbug.qa.serviec.MessageService;
import com.zingbug.qa.serviec.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message);
    }

    @Override
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }

    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId,offset,limit);
    }

    @Override
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }

    @Override
    public void hasReadMessage(String conversationId) {
        messageDao.updateHasRead(conversationId,1);
    }
}
