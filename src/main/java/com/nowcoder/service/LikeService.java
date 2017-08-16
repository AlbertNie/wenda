package com.nowcoder.service;

import com.nowcoder.model.EntityType;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by albert on 2017/8/14.
 */
@Service
public class LikeService {

    public long like(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        JedisUtil.sAdd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        JedisUtil.srem(disLikeKey,String.valueOf(userId));

        return JedisUtil.scard(likeKey);

    }

    public long disLike(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);

        JedisUtil.sAdd(disLikeKey,String.valueOf(userId));
        JedisUtil.srem(likeKey,String.valueOf(userId));

        return JedisUtil.scard(likeKey);
    }

    public long likeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return JedisUtil.scard(likeKey);
    }


    public int getStatus(int userId,int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);

        if (JedisUtil.Sismenber(likeKey,String.valueOf(userId)))
            return 1;
        else if (JedisUtil.Sismenber(disLikeKey,String.valueOf(userId)))
            return -1;
        else return 0;
    }




}
