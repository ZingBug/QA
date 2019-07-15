package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
public interface MessageService {

    int addMessage(Message message);

    List<Message> getConversationDetail(String conversationId,int offset,int limit);

    List<Message> getConversationList(int userId,int offset,int limit);

    int getConversationUnreadCount(int userId,String conversationId);

    void hasReadMessage(String conversationId);
}
