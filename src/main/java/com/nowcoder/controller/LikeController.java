package com.nowcoder.controller;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventProducer;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * Created by albert on 2017/8/11.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    JedisUtil jedisUtil;


    @RequestMapping(value = "/like")
    @ResponseBody
    public String addLike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);

        long likeCount = likeService.like(hostHolder.getUser().getId(),EntityType.TYPE_COMMENT,commentId);
        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EvenModel(EvenType.LIKE).setEntityType(EntityType.TYPE_COMMENT)
                                .setEntityId(commentId).setActorId(hostHolder.getUser().getId())
                                .setEntityOwnerId(comment.getUserId())
                                .setExt("questionId",String.valueOf(comment.getEntityId())));

        return WendaUtil.toJsonCode(0,String.valueOf(likeCount));

    }

    @RequestMapping(value = "/dislike")
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);

        long likeCount = likeService.disLike(hostHolder.getUser().getId(),EntityType.TYPE_COMMENT,commentId);

        return WendaUtil.toJsonCode(0,String.valueOf(likeCount));

    }

}
