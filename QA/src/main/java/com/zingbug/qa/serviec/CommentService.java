package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.Comment;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/1.
 */
public interface CommentService {

    List<Comment> getCommentByEntity(int entityId,int entityType);

    int addComment(Comment comment);

    int getCommentCount(int entityId,int entityType);

    void deleteComment(int entityId,int entityType);

    Comment selectById(int id);

    int getCommentCountByUser(int userId);
}
