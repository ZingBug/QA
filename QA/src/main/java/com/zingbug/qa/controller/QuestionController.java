package com.zingbug.qa.controller;

import com.zingbug.qa.comm.Const;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.model.ViewObject;
import com.zingbug.qa.pojo.Comment;
import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.*;
import com.zingbug.qa.util.DateUtil;
import com.zingbug.qa.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Controller
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam(value = "title") String title,
                              @RequestParam(value = "content") String content)
    {
        try {
            Question question=new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreateDate(DateUtil.getCurrentDate());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null)
            {
                //question.setUserId(Const.ANONYMOUS_USERID);//添加匿名用户
                return JSONUtil.getJSONString(999);//提示未登录
            }
            else
            {
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question)>0)
            {
                return JSONUtil.getJSONString(0);//返回成功
            }
        }
        catch (Exception e)
        {
            log.error("增加问题失败"+e.getMessage());
        }
        return JSONUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}",method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable(value = "qid") int qid)
    {
        Question question=questionService.selectById(qid);
        model.addAttribute("question",question);

        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos=new ArrayList<>();
        for(Comment comment:commentList)
        {
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.selectById(comment.getUserId()));
            if(hostHolder.getUser()==null)
            {
                vo.set("liked",0);
            }
            else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);

        //获取关注的用户信息
        List<ViewObject> followUsers=new ArrayList<>();
        List<Integer> users=followService.getFollowers(EntityType.ENTITY_QUESTION,qid,20);
        for(Integer userId:users)
        {
            ViewObject vo=new ViewObject();
            User user=userService.selectById(userId);
            if(user==null)
            {
                continue;
            }
            vo.set("name",user.getName());
            vo.set("headUrl",user.getHeadUrl());
            vo.set("id",user.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }
}
