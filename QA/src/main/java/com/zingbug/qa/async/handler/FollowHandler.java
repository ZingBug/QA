package com.zingbug.qa.async.handler;

import com.zingbug.qa.async.EventHandler;
import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.comm.Const;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.pojo.Message;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.MessageService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.MarshalException;
import java.util.Arrays;
import java.util.List;

/**
 * 关注handler
 * Created by ZingBug on 2019/7/3.
 */
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(Const.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreateDate(DateUtil.getCurrentDate());

        User user = userService.selectById(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_USER) {
            //关注用户
            message.setContent("用户" + user.getName()
                    + "关注了你，http://127.0.0.1:8080/user/" + model.getActorId());
        } else if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        }
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
