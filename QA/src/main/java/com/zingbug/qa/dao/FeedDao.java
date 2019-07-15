package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.Feed;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Repository
public interface FeedDao {

    int addFeed(Feed feed);

    Feed selectById(int id);

    List<Feed> selectFeedsByUser(@Param(value = "maxId") int maxId,
                                 @Param(value = "userIds") List<Integer> userIds,
                                 @Param(value = "count") int count);
}
