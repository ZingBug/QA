package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.serviec.FollowService;
import com.zingbug.qa.util.DateUtil;
import com.zingbug.qa.util.JedisAdapter;
import com.zingbug.qa.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Service
@Slf4j
public class FollowServiceImpl implements FollowService {

    @Autowired
    private JedisAdapter jedisAdapter;


    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        //采用事务
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //实体的粉丝增加当前用户
        tx.zadd(followerKey, DateUtil.getCurrentTime(), String.valueOf(userId));
        //当前用户对这类实体关注+1
        tx.zadd(followeeKey, DateUtil.getCurrentTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 用户取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        //采用事务
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //实体的粉丝增加当前用户
        tx.zrem(followerKey, String.valueOf(userId));
        //当前用户对这类实体关注+1
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 得到某个实体的所有粉丝列表
     *
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    /**
     * 得到某个实体的所有粉丝列表，加入分页
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
    }

    /**
     * 得到某人所有的关注列表
     *
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    /**
     * 得到某人所有的关注列表，加入分页
     *
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset + count));
    }

    /**
     * 得到某个实体的所有粉丝数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    /**
     * 得到用户的关注数量
     *
     * @param userId
     * @param entityType
     * @return
     */
    @Override
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     * 判断用户是否关注了某个实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }

    /**
     * set集合转为list
     * @param set
     * @return
     */
    private List<Integer> getIdsFromSet(Set<String> set) {
        List<Integer> list = new ArrayList<>();
        for (String str : set) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }
}
