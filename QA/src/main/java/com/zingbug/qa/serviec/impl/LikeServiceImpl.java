package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.serviec.LikeService;
import com.zingbug.qa.util.JedisAdapter;
import com.zingbug.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZingBug on 2019/7/2.
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public long getLikeCount(int entityType, int entityId) {
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }

    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)))
        {
            return 1;
        }
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId))?-1:0;
    }

    @Override
    public long like(int userId, int entityType, int entityId) {
        //增加赞
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        //删除踩
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);

    }

    @Override
    public long disLike(int userId, int entityType, int entityId) {
        //增加踩
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        //删除赞
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
}
