package com.zingbug.qa.serviec;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/3.
 */
public interface FollowService {

    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean follow(int userId,int entityType,int entityId);

    /**
     * 用户取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean unfollow(int userId,int entityType,int entityId);

    /**
     * 得到某个实体的所有粉丝列表
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    List<Integer> getFollowers(int entityType,int entityId,int count);

    /**
     * 得到某个实体的所有粉丝列表，加入分页
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowers(int entityType,int entityId,int offset,int count);

    /**
     * 得到某人所有的关注列表
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    List<Integer> getFollowees(int userId,int entityType,int count);

    /**
     * 得到某人所有的关注列表，加入分页
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowees(int userId,int entityType,int offset,int count);

    /**
     * 得到某个实体的所有粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    long getFollowerCount(int entityType,int entityId);

    /**
     * 得到用户的关注数量
     * @param entityType
     * @param userId
     * @return
     */
    long getFolloweeCount(int userId,int entityType);

    /**
     * 判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean isFollower(int userId,int entityType,int entityId);
}
