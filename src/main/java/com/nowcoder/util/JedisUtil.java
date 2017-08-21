package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    public static List<String> lRange(String key, long start, long stop){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.lrange(key,start,stop);
        } catch (Exception e) {
            logger.error("redis 获取队列元素失败：5 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
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

    public static long zadd(String key,double core,String value){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.zadd(key,core,value);
        } catch (Exception e) {
            logger.error("redis 存进有序set失败：7 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return 0;
    }

    public static long zrem(String key,String value){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.zrem(key,value);
        } catch (Exception e) {
            logger.error("redis 删除有序set失败：8 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return 0;
    }

    public static Set<String> zrevrangeByScore(String key, String max, String min, int offsit, int limit){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key,max,min,offsit,limit);
        } catch (Exception e) {
            logger.error("redis 返回有序set失败：9 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
    }

    public static long zcard(String key){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("redis 返回有序set键总数失败：9 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return -1;
    }

    public static Jedis jedis(){
        return pool.getResource();
    }

    public static Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("标记事物块失败");
        }
        return null;
    }

    public static List<Object> exec(Transaction multi,Jedis jedis){
        try {
            return multi.exec();
        } catch (Exception e) {
            logger.error("redis事务执行失败" + e.getMessage());
        } finally {
            if (multi != null){
                try {
                    multi.close();
                } catch (IOException e) {
                    logger.error("redis事务标记块关闭失败"+e.getMessage());
                }
            }
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }


    public static Object zscore(String key, String value) {
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            return jedis.zscore(key,value);
        } catch (Exception e) {
            logger.error("redis 查询键值是否存在出错：10 " + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
    }
}



















