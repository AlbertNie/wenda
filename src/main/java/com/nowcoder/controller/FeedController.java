package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2017/8/18.
 */
@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JedisUtil jedisUtil;


    @RequestMapping("/pullfeeds")
    public String getPullFeeds(Model model){
        User localUser = hostHolder.getUser();
        List<Integer> followees = new ArrayList<>();
        if (localUser != null)
            followees = followService.getFollowees(EntityType.TYPE_USER,localUser.getId(),0,Integer.MAX_VALUE);
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    @RequestMapping("/pushfeeds")
    public String getPushFeeds(Model model){
        User localUser = hostHolder.getUser();
        List<String> feedsId = jedisUtil.lRange(RedisKeyUtil.getFeedskey(localUser.getId()),0,10);
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedsId) {
            Feed feed = feedService.selectFeedById(Integer.parseInt(feedId));
            if (feed == null)
                continue;
            feeds.add(feed);
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
