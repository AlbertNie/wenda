package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by albert on 2017/8/14.
 */
@Service
public class JedisUtil implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private static JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool();
    }

    public static void sAdd(String key, String value){
        Jedis jedis = null;

        try {

            jedis = pool.getResource();

            jedis.sadd(key,value);

        } catch (Exception e) {
            logger.error("redis 添加失败：1" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

    }


    public static long scard(String key){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("redis 添加失败：2" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return 0;
    }

    public static void srem(String key, String value){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            jedis.srem(key,value);
        } catch (Exception e) {
            logger.error("redis 添加失败：3" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public static boolean Sismenber(String key,String value){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        } catch (Exception e) {
            logger.error("redis 添加失败：4" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return false;
    }

    public static void lPush(String key, String value){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            jedis.lpush(key,value);
        } catch (Exception e) {
            logger.error("redis 添加失败：5 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public static List<String> brpop(String key){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.brpop(0,key);
        } catch (Exception e) {
            logger.error("redis 取出队列失败：6 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
    }




}



















