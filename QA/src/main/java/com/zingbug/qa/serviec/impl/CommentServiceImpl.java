package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.CommentDao;
import com.zingbug.qa.pojo.Comment;
import com.zingbug.qa.serviec.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId,entityType);
    }

    @Override
    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId,entityType);
    }

    @Override
    public void deleteComment(int entityId, int entityType) {
        commentDao.updateStatus(entityId,entityType,1);
    }

    @Override
    public Comment selectById(int id) {
        return commentDao.selectById(id);
    }

    @Override
    public int getCommentCountByUser(int userId) {
        return commentDao.getCommentCountByUser(userId);
    }
}
