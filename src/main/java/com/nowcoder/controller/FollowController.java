package com.nowcoder.controller;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventProducer;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2017/8/17.
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/followUser",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);
        if (userService.getUser(userId) == null)
            return WendaUtil.toJsonCode(1,"用户不存在");
        boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.TYPE_USER,userId);
        if (result){
            EvenModel evenModel = new EvenModel(EvenType.FOLLOW);
            evenModel.setActorId(hostHolder.getUser().getId());
            evenModel.setEntityType(EntityType.TYPE_USER);
            evenModel.setEntityId(userId);
            eventProducer.fireEvent(evenModel);
        }
        return result?WendaUtil.toJsonCode(0,
                String.valueOf(followService.getFollowerCount(EntityType.TYPE_USER,userId))):WendaUtil.toJsonCode(1,
                "关注失败");
    }

    @RequestMapping(value = "/followQuestion",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);
        if (questionService.selectQuestionById(questionId) == null)
            return WendaUtil.toJsonCode(1,"问题不存在");
        boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.TYPE_QUESTION,questionId);
        if (result){
            EvenModel evenModel = new EvenModel(EvenType.FOLLOW);
            evenModel.setActorId(hostHolder.getUser().getId());
            evenModel.setEntityType(EntityType.TYPE_QUESTION);
            evenModel.setEntityId(questionId);
            eventProducer.fireEvent(evenModel);
        }
        return result?WendaUtil.toJsonCode(0,
                String.valueOf(followService.getFollowerCount(EntityType.TYPE_QUESTION,
                        questionId))):WendaUtil.toJsonCode(1,"关注失败");
    }

    @RequestMapping(value = "/unfollowUser",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String unFollowUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);
        if (userService.getUser(userId) == null)
            return WendaUtil.toJsonCode(1,"用户不存在");
        boolean result = followService.unFollow(hostHolder.getUser().getId(), EntityType.TYPE_USER,userId);
        return result?WendaUtil.toJsonCode(0,
                String.valueOf(followService.getFollowerCount(EntityType.TYPE_USER,userId))):WendaUtil.toJsonCode(1,
                "取消关注失败");
    }

    @RequestMapping(value = "/unfollowQuestion",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String unFollowQuestion(@RequestParam("questionId") int questionId){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);
        if (questionService.selectQuestionById(questionId) == null)
            return WendaUtil.toJsonCode(1,"问题不存在");
        boolean result = followService.unFollow(hostHolder.getUser().getId(), EntityType.TYPE_QUESTION,questionId);
        return result?WendaUtil.toJsonCode(0,
                String.valueOf(followService.getFollowerCount(EntityType.TYPE_QUESTION,questionId))):WendaUtil.toJsonCode(1,
                "取消关注失败");
    }


    @RequestMapping(value = "/user/{userId}/followers",method = {RequestMethod.GET,RequestMethod.POST})

    public String followers(@PathVariable("userId") int userId,
                            Model model){
        User thisUser = hostHolder.getUser();
        User curUser = userService.getUser(userId);
        long followerCount = followService.getFollowerCount(EntityType.TYPE_USER,userId);
        model.addAttribute("curUser",curUser);
        model.addAttribute("followerCount",followerCount);
        List<Integer> list = followService.getFollowers(EntityType.TYPE_USER,userId,0,10);
        List<ViewObject> followers = new ArrayList<>();
        for (int followerId : list) {
            ViewObject vo = new ViewObject();
            User user = userService.getUser(followerId);
            vo.set("user",user);
            if (followService.isFollower(thisUser.getId(),EntityType.TYPE_USER,user.getId()))
                vo.set("followed",true);
            vo.set("followerCount",followService.getFollowerCount(EntityType.TYPE_USER,user.getId()));
            vo.set("followeeCount",followService.getFolloweeCount(EntityType.TYPE_USER,user.getId()));
            vo.set("commentCount",commentService.getCommentCountByUserId(user.getId(),EntityType.TYPE_COMMENT));
            vo.set("likeCount",user.getLikeCount());
            followers.add(vo);
        }
        model.addAttribute("followers",followers);
        return "followers";
    }

    @RequestMapping(value = "/user/{userId}/followees",method = {RequestMethod.GET,RequestMethod.POST})

    public String followees(@PathVariable("userId") int userId,
                            Model model){
        User thisUser = hostHolder.getUser();
        User curUser = userService.getUser(userId);
        long followeeCount = followService.getFolloweeCount(EntityType.TYPE_USER,userId);
        model.addAttribute("curUser",curUser);
        model.addAttribute("followeeCount",followeeCount);
        List<Integer> list = followService.getFollowees(EntityType.TYPE_USER,userId,0,10);
        List<ViewObject> followees = new ArrayList<>();
        for (int followeeId : list) {
            ViewObject vo = new ViewObject();
            User user = userService.getUser(followeeId);
            vo.set("user",user);
            if (followService.isFollower(thisUser.getId(),EntityType.TYPE_USER,user.getId()))
                vo.set("followed",true);
            vo.set("followerCount",followService.getFollowerCount(EntityType.TYPE_USER,user.getId()));
            vo.set("followeeCount",followService.getFolloweeCount(EntityType.TYPE_USER,user.getId()));
            vo.set("commentCount",commentService.getCommentCountByUserId(user.getId(),EntityType.TYPE_COMMENT));
            vo.set("likeCount",user.getLikeCount());
            followees.add(vo);
        }
        model.addAttribute("followees",followees);
        return "followees";
    }


}
