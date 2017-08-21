package com.nowcoder.util;

import com.nowcoder.model.EntityType;

/**
 * Created by albert on 2017/8/15.
 */
public class RedisKeyUtil {
    public static final String TYPE_LIKE = "LIKE";
    public static final String TYPE_DISLIKE = "DISLIKE";
    public static final String EVENT_QUEUE = "EVENT_QUEUE";
    public static final String UNACTIVE = "UN_ACTIVE";
    public static final String FOLLOWER = "FOLLOWER";
    public static final String FOLLOWEE = "FOLLOWEE";
    public static final String FEEDSKEY = "FEEDSKEY";


    public static String getLikeKey(int entityType, int entityId){
        return TYPE_LIKE + entityType + ":" + entityId;
    }

    public static String getDisLikeKey(int entityType, int entityId){
        return TYPE_DISLIKE + entityType + ":" + entityId;
    }

    public static String getEventQueueKey(){
        return EVENT_QUEUE;
    }

    public static String getUnactiveKey(){
        return UNACTIVE;
    }

    public static String getFollowerKey(int entityType, int entityId){
        return FOLLOWER+":"+String.valueOf(entityType)+":"+entityId;
    }

    public static String getFolloweeKey(int userId, int entityType){
        return FOLLOWEE+":"+String.valueOf(userId)+":"+entityType;
    }

    public static String getFeedskey(int userId){
        return FEEDSKEY + ":" + userId;
    }
}
