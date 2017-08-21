package com.nowcoder.service;

import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by albert on 2017/8/16.
 */
@Service
public class FollowService {
    @Autowired
    JedisUtil jedisUtil;

    //关注一个对象
    public boolean follow(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();

        Jedis jedis = JedisUtil.jedis();
        Transaction transaction = JedisUtil.multi(jedis);
        //添加这个实体的粉丝有哪些，用userId来代替
        JedisUtil.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //添加这个关注者关注的这个类型的实体的ID；
        JedisUtil.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> list = JedisUtil.exec(transaction,jedis);
        return list!=null?true:false;
    }


    //取消关注一个对象
    public boolean unFollow(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);

        Jedis jedis = JedisUtil.jedis();
        Transaction transaction = JedisUtil.multi(jedis);
        //删除这个实体的粉丝有哪些，用userId来代替
        JedisUtil.zrem(followerKey,String.valueOf(userId));
        //删除这个关注者关注的这个类型的实体的ID；
        JedisUtil.zrem(followeeKey,String.valueOf(entityId));
        List<Object> list = JedisUtil.exec(transaction,jedis);
        return list!=null?true:false;
    }


    //查询是否关注了某个对象
    public boolean isFollower(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return JedisUtil.zscore(followerKey,String.valueOf(userId)) != null;
    }

    //返回粉丝的集合
    public List<Integer> getFollowers(int entityType, int entityId,int offsit,int limit){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        Set<String> set = JedisUtil.zrevrangeByScore(followerKey,"+inf","-inf",offsit,limit);
        return setToList(set);
    }

    private List<Integer> setToList(Set<String> set){
        List<Integer> list = new ArrayList<>();
        for (String str : set) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    //返回关注的实体的集合
    public List<Integer> getFollowees(int entityType, int userId,int offsit,int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Set<String> set = JedisUtil.zrevrangeByScore(followeeKey,"+inf","-inf",offsit,limit);
        return setToList(set);
    }

    //返回粉丝的数量
    public long getFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisUtil.zcard(followerKey);
    }
    //返回关注的数量
    public long getFolloweeCount(int entityType, int userId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisUtil.zcard(followeeKey);
    }
}
