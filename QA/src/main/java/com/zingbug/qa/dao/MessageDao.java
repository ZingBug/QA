package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Repository
public interface MessageDao {

    int addMessage(Message message);

    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    void updateHasRead(@Param("conversationId") String conversationId,@Param("hasRead") int hasRead);
}
