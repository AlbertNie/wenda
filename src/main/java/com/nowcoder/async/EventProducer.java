package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by albert on 2017/8/15.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisUtil jedisUtil;

    public boolean fireEvent(EvenModel event){
        try {
            String json = JSONObject.toJSONString(event);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisUtil.lPush(key,json);
            return true;
        } catch (Exception e) {
            logger.error("事件创建出错: " + e.getMessage());
            return false;
        }
    }
}
