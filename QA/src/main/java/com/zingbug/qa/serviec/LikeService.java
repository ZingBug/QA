package com.zingbug.qa.serviec;

/**
 * Created by ZingBug on 2019/7/2.
 */
public interface LikeService {

    /**
     * 返回点赞数量
     */
    long getLikeCount(int entityType,int entityId);

    /**
     * 获取点赞状态
     * @return 1:赞 ; 0:无赞无踩  ; -1:踩
     */
    int getLikeStatus(int userId,int entityType,int entityId);

    /**
     * 点赞
     * @return 当前点赞数量
     */
    long like(int userId,int entityType,int entityId);

    /**
     * 踩
     * @return 当前点赞数量
     */
    long disLike(int userId,int entityType,int entityId);
}
