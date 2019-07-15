package com.zingbug.qa.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zingbug.qa.async.EventHandler;
import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.pojo.Feed;
import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.FeedService;
import com.zingbug.qa.serviec.FollowService;
import com.zingbug.qa.serviec.QuestionService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.DateUtil;
import com.zingbug.qa.util.JSONUtil;
import com.zingbug.qa.util.JedisAdapter;
import com.zingbug.qa.util.RedisKeyUtil;
import org.attoparser.trace.MarkupTraceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.nio.cs.ext.MacArabic;

import java.util.*;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private FollowService followService;

    @Override
    public void doHandle(EventModel model) {
//        //仅供测试使用
//        Random random=new Random();
//        model.setActorId(1+random.nextInt(10));

        Feed feed = new Feed();
        feed.setCreateDate(DateUtil.getCurrentDate());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);
        //进行推送操作
        List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);//获取所有粉丝
        followers.add(0);//系统队列
        for(Integer follower:followers)// 给所有粉丝推事件
        {
            String timeLineKey= RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timeLineKey,String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.selectById(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||
                model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION) {//评论或者关注问题
            Question question = questionService.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
}
