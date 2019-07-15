package com.zingbug.qa.controller;

import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.model.ViewObject;
import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.CommentService;
import com.zingbug.qa.serviec.FollowService;
import com.zingbug.qa.serviec.QuestionService;
import com.zingbug.qa.serviec.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Controller
@Slf4j
public class HomeController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FollowService followService;

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET})
    public String index(Model model)
    {
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET})
    public String index(Model model, @PathVariable("userId") int userId)
    {

        model.addAttribute("vos",getQuestions(userId,0,10));

        User user=userService.selectById(userId);
        ViewObject vo=new ViewObject();
        vo.set("user",user);
        vo.set("commentCount",commentService.getCommentCountByUser(userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        if(hostHolder.getUser()!=null)
        {
            vo.set("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId));
        }
        else
        {
            vo.set("followed",false);
        }
        model.addAttribute("profileUser", vo);

        return "profile";
    }

    private List<ViewObject> getQuestions(int userId,int offset,int limit)
    {
        List<Question> questions=questionService.selectLatestQuestions(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questions)
        {
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.selectById(question.getUserId()));
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));//问题的关注人数
            vos.add(vo);
        }
        return vos;
    }


}
