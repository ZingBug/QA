package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.Feed;

import java.util.List;

/**
 * timeline推拉新事件
 * Created by ZingBug on 2019/7/3.
 */
public interface FeedService {

    /**
     * 拉模式
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> getFeedsByUser(int maxId,List<Integer> userIds,int count);

    boolean addFeed(Feed feed);

    /**
     * 推模式
     * @param id
     * @return
     */
    Feed getById(int id);
}
