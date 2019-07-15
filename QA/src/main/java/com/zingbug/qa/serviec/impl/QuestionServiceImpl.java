package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.QuestionDao;
import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.serviec.QuestionService;
import com.zingbug.qa.serviec.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public int addQuestion(Question question) {
        //HTML过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return questionDao.addQuestion(question)>0?question.getId():0;
    }

    @Override
    public Question selectById(int id) {
        return questionDao.selectById(id);
    }

    @Override
    public int deleteById(int id) {
        return questionDao.deleteById(id);
    }

    @Override
    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return questionDao.updateCommentCount(id,commentCount);
    }
}
