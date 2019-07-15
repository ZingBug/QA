package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Repository
public interface QuestionDao {

    int addQuestion(Question question);

    Question selectById(int id);

    int deleteById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
