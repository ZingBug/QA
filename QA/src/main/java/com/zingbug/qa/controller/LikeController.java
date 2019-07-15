package com.zingbug.qa.controller;

import com.alibaba.fastjson.JSON;
import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventProducer;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.pojo.Comment;
import com.zingbug.qa.serviec.CommentService;
import com.zingbug.qa.serviec.LikeService;
import com.zingbug.qa.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZingBug on 2019/7/2.
 */
@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam(value = "commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);//提示未登录
        }

        //异步发送站内信
        Comment comment = commentService.selectById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return JSONUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam(value = "commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);//提示未登录
        }
        long disLikeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return JSONUtil.getJSONString(0, String.valueOf(disLikeCount));
    }
}
