package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.Question;

import java.util.List;

/**
 * Created by ZingBug on 2019/6/29.
 */
public interface QuestionService {

    int addQuestion(Question question);

    Question selectById(int id);

    int updateCommentCount(int id,int commentCount);

    int deleteById(int id);

    List<Question> selectLatestQuestions(int userId,int offset,int limit);
}
