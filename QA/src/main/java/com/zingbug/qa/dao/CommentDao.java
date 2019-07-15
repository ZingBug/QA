package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Repository
public interface CommentDao {

    int addComment(Comment comment);

    Comment selectById(int id);

    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

    int getCommentCountByUser(int userId);
}
