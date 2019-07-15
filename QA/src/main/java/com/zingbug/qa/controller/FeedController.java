package com.zingbug.qa.controller;

import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.pojo.Feed;
import com.zingbug.qa.serviec.FeedService;
import com.zingbug.qa.serviec.FollowService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.JedisAdapter;
import com.zingbug.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Controller
public class FeedController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FeedService feedService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    //拉模式
    @RequestMapping(value = {"/pullfeeds"},method = {RequestMethod.GET})
    public String getPullFeeds(Model model)
    {
        int localUserId=hostHolder.getUser()== null?0:hostHolder.getUser().getId();
        List<Integer> followees=new ArrayList<>();
        if(localUserId!=0)
        {
            followees=followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);//取出所有的关注者
        }
        List<Feed> feeds=feedService.getFeedsByUser(Integer.MAX_VALUE,followees,10);//找出所有与关注着相关的事件（评论、关注问题）

        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    //推模式
    @RequestMapping(value = {"/pushfeeds"},method = {RequestMethod.GET})
    public String getPushFeeds(Model model)
    {
        int localUserId=hostHolder.getUser()==null?0:hostHolder.getUser().getId();
        String timeLineKey= RedisKeyUtil.getTimelineKey(localUserId);
        List<String> feedIds=jedisAdapter.lrange(timeLineKey,0,10 );
        List<Feed> feeds=new ArrayList<>();
        for(String feedId:feedIds)
        {
            Feed feed=feedService.getById(Integer.parseInt(feedId));
            if(feed!=null)
            {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds",feeds);
        return "feeds";

    }
}
