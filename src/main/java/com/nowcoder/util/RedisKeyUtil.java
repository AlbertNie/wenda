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
}
