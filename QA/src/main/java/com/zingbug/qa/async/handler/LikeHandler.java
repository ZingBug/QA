package com.zingbug.qa.async.handler;

import com.zingbug.qa.async.EventHandler;
import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.comm.Const;
import com.zingbug.qa.pojo.Message;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.MessageService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ZingBug on 2019/7/2.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFromId(Const.SYSTEM_USERID);//系统管理员
        message.setToId(model.getEntityOwnerId());
        message.setCreateDate(DateUtil.getCurrentDate());
        User user=userService.selectById(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的评论，httpo://127.0.0.1:8080/question/"+model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
