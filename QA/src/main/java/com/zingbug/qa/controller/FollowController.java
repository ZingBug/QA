package com.zingbug.qa.controller;

import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventProducer;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.model.ViewObject;
import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.CommentService;
import com.zingbug.qa.serviec.FollowService;
import com.zingbug.qa.serviec.QuestionService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Controller
@Slf4j
public class FollowController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

        //返回关注的人数
        return JSONUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

//        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
//                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
//                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

        //返回关注的人数
        return JSONUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);
        }
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return JSONUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        //返回关注信息
        return JSONUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);
        }
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return JSONUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

//        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
//                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
//                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        //返回关注信息
        return JSONUtil.getJSONString(ret ? 0 : 1, info);
    }

    /**
     * 粉丝页面
     *
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable(value = "uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.selectById(userId));
        return "followers";
    }

    /**
     * 关注者页面
     *
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable(value = "uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.selectById(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.selectById(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getCommentCountByUser(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                //判断是否是相互关注
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }


}
