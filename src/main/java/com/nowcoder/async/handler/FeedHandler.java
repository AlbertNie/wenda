package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventHandler;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by albert on 2017/8/15.
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    MessageServive messageServive;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    FollowService followService;

    @Override
    public void doHandle(EvenModel event) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(event.getActorId());
        feed.setType(event.getType().getValue());
        feed.setData(getData(event));
        if (feed.getData() == null)
            return;
        //这是使用拉的方式，每个用户自己来数据库拉取新鲜事
        feedService.addFeed(feed);
        //使用推的方式，将feed的id主动推到粉丝用户的新鲜事列表中
        List<Integer> followers = followService.getFollowers(EntityType.TYPE_USER,event.getActorId(),0,Integer.MAX_VALUE);
        for (int follower : followers) {
            jedisUtil.lPush(RedisKeyUtil.getFeedskey(follower),String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EvenType> getSupportEventTypes() {
        return Arrays.asList(new EvenType[]{EvenType.LIKE, EvenType.COMMENT,EvenType.QUESTION,EvenType.FOLLOW});
    }

    private String getData(EvenModel evenModel){
        Map<String,String> map = new HashMap<>();
        User user = userService.getUser(evenModel.getActorId());
        if (user == null)
            return null;
        map.put("userName",user.getName());
        map.put("headUrl",user.getHeadUrl());
        map.put("likeCount",String.valueOf(user.getLikeCount()));
        map.put("followCount",String.valueOf(followService.getFolloweeCount(EntityType.TYPE_USER,user.getId())));
       //处理回答问题的data
        if (evenModel.getType() == EvenType.COMMENT){
            Comment comment = commentService.getCommentById(evenModel.getEntityId());
            if (comment.getContent().length()>50)
                map.put("commentContent",comment.getContent().substring(0,50));
            else
                map.put("commentContent",comment.getContent());
           Question question = questionService.selectQuestionById(comment.getEntityId());
           if (question == null)
               return null;
           map.put("questionId",String.valueOf(question.getId()));
           map.put("questionTitle", question.getTitle());
           map.put("commentCount", toString().valueOf(question.getCommentCount()));

        }
       //处理like的data
        if (evenModel.getType() == EvenType.LIKE){
            Comment comment = commentService.getCommentById(evenModel.getEntityId());
            if (comment == null)
                return null;
            map.put("commentId",String.valueOf(comment.getId()));
            if (comment.getContent().length()>50)
                map.put("commentContent",comment.getContent().substring(0,50));
            else
                map.put("commentContent",comment.getContent());
            map.put("commentUserName",userService.getUser(comment.getUserId()).getName());
            Question question = questionService.selectQuestionById(comment.getEntityId());
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            map.put("commentCount",String.valueOf(question.getCommentCount()));


        }
       //处理发布问题的data
        if (evenModel.getType() == EvenType.QUESTION){
            Question question = questionService.selectQuestionById(evenModel.getEntityId());
            if (question == null)
                return null;
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            map.put("questionContent",question.getContent());
        }
       //处理关注的data
        if (evenModel.getType() == EvenType.FOLLOW){
            if (evenModel.getEntityType() == EntityType.TYPE_USER) {
                User followUser = userService.getUser(evenModel.getEntityId());
                if (followUser == null)
                    return null;
                map.put("followUserId", String.valueOf(user.getId()));
                map.put("followUserName", followUser.getName());
                map.put("followUserHeadUrl", followUser.getHeadUrl());
                map.put("followUserLikeCount", String.valueOf(followUser.getLikeCount()));
            }
            if (evenModel.getEntityType() == EntityType.TYPE_QUESTION){
                Question question = questionService.selectQuestionById(evenModel.getEntityId());
                if (question == null)
                    return null;
                map.put("questionId",String.valueOf(question.getId()));
                map.put("questionTitle",question.getTitle());
                if (question.getContent().length() > 50)
                    map.put("questionContent",question.getContent().substring(50));
                else
                    map.put("questionContent",question.getContent());
                map.put("commentCount",String.valueOf(question.getCommentCount()));
            }
        }
        return JSONObject.toJSONString(map);
    }
}
