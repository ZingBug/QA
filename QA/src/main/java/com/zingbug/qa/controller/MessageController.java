package com.zingbug.qa.controller;

import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.model.ViewObject;
import com.zingbug.qa.pojo.Message;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.MessageService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.DateUtil;
import com.zingbug.qa.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Controller
@Slf4j
public class MessageController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam(value = "toName") String toName,
                             @RequestParam(value = "content") String content)
    {
        try {
            if(hostHolder.getUser()==null)
            {
                return JSONUtil.getJSONString(999,"未登录");
            }

            User user=userService.selectByName(toName);
            if(user==null)
            {
                return JSONUtil.getJSONString(1,"用户不存在");
            }
            Message message=new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreateDate(DateUtil.getCurrentDate());
            messageService.addMessage(message);
            return JSONUtil.getJSONString(0);//发信成功
        }
        catch (Exception e)
        {
            log.error("发送消息失败"+e.getMessage());
            return JSONUtil.getJSONString(1,"发信失败");
        }
    }

    /**
     * 消息对话详情页
     */
    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam(value = "conversationId") String conversationId)
    {
        try {
            messageService.hasReadMessage(conversationId);//确定阅读
            List<Message> conversationList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<>();
            for(Message message:conversationList)
            {

                ViewObject vo=new ViewObject();
                vo.set("message",message);
                User user=userService.selectById(message.getFromId());
                if(user==null)
                {
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("message",messages);
        }
        catch (Exception e)
        {
            log.error("获取详情消息失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String conversationList(Model model)
    {
        try {
            int localUserId=hostHolder.getUser().getId();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations=new ArrayList<>();
            for(Message message:conversationList)
            {
                ViewObject vo=new ViewObject();
                vo.set("conversation",message);
                int targetId=message.getFromId()==localUserId?message.getToId():message.getFromId();
                User user=userService.selectById(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);

        }catch (Exception e)
        {
            log.error("获取站内信列表失败"+e.getMessage());
        }
        return "letter";
    }
}
