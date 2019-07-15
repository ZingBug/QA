package com.zingbug.qa.controller;

import com.zingbug.qa.async.EventModel;
import com.zingbug.qa.async.EventProducer;
import com.zingbug.qa.async.EventType;
import com.zingbug.qa.comm.Const;
import com.zingbug.qa.model.EntityType;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.pojo.Comment;
import com.zingbug.qa.serviec.CommentService;
import com.zingbug.qa.serviec.QuestionService;
import com.zingbug.qa.serviec.SensitiveService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Controller
@Slf4j
public class CommentController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private EventProducer eventProducer;


    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam(value = "questionId") int questionId,
                             @RequestParam(value = "content") String content) {
        try {
            //过滤content
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);
            //新建comment
            Comment comment = new Comment();
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(Const.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreateDate(DateUtil.getCurrentDate());
            comment.setStatus(0);

            commentService.addComment(comment);
            //更新题目里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

            //异步发送评论消息作为timeline
            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setEntityOwnerId(questionId)
                    .setEntityId(comment.getId())
                    .setEntityType(EntityType.ENTITY_COMMENT)
                    .setActorId(comment.getUserId()));
        } catch (Exception e) {
            log.error("增加评论失败" + e.getMessage());
        }


        return "redirect:/question/" + String.valueOf(questionId);
    }

}
