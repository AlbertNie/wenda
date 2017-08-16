package com.nowcoder.async;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.CommentController;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albert on 2017/8/15.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    JedisUtil jedisUtil;

    private Map<EvenType,List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans!=null){
            for (String handler : beans.keySet()) {
                List<EvenType> events = beans.get(handler).getSupportEventTypes();
                for (EvenType event : events) {
                    if (!config.containsKey(event))
                        config.put(event,new ArrayList<>());
                    config.get(event).add(beans.get(handler));
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisUtil.brpop(key);
                    for (String event : events) {
                        if (event.equals(key))
                            continue;
                        EvenModel evenModel = JSONObject.parseObject(event,EvenModel.class);
                        if (!config.containsKey(evenModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(evenModel.getType())) {
                            handler.doHandle(evenModel);
                        }

                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
