package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.FeedDao;
import com.zingbug.qa.pojo.Feed;
import com.zingbug.qa.serviec.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedDao feedDao;

    /**
     * 拉模式
     *
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    @Override
    public List<Feed> getFeedsByUser(int maxId, List<Integer> userIds, int count) {
        return feedDao.selectFeedsByUser(maxId,userIds,count);
    }

    @Override
    public boolean addFeed(Feed feed) {
        feedDao.addFeed(feed);
        return feed.getId()>0;
    }

    /**
     * 推模式
     *
     * @param id
     * @return
     */
    @Override
    public Feed getById(int id) {
        return feedDao.selectById(id);
    }
}
