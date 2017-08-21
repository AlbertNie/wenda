package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;


     @RequestMapping(path = {"/","/index"})
     public String index(Model model){
         List<ViewObject> vos = getQuestionswithId(0,0,5);
         model.addAttribute("vos",vos);
         return "index";
     }

     @RequestMapping("/user/{userId}")
     public String userhome(@PathVariable("userId") int id,
                            Model model){
         List<ViewObject> vos = getQuestionswithId(id,0,5);
         model.addAttribute("vos",vos);
         User otherUser = userService.getUser(id);
         model.addAttribute("otherUser",otherUser);

        //多少个关注者，多少个提问，多少个回答，获得多少个赞同
         long followerCount = followService.getFollowerCount(EntityType.TYPE_USER,otherUser.getId());

         long followeeCount = followService.getFolloweeCount(EntityType.TYPE_USER,otherUser.getId());

         int questionCount = questionService.getQuestionCountByUserId(otherUser.getId());

         int commentCount = commentService.getCommentCountByUserId(otherUser.getId(),EntityType.TYPE_QUESTION);

         int likeCount = otherUser.getLikeCount();
         if (followService.isFollower(hostHolder.getUser().getId(),EntityType.TYPE_USER,id))
             model.addAttribute("followed",true);

         model.addAttribute("followerCount",followerCount);
         model.addAttribute("followeeCount",followeeCount);
         model.addAttribute("questionCount",questionCount);
         model.addAttribute("commentCount",commentCount);
         model.addAttribute("likeCount",likeCount);

         return "index";
     }

     private List<ViewObject> getQuestionswithId(int userId, int offset, int limit){
         List<Question> questions = questionService.getLatestQuestions(userId,offset,limit);
         List<ViewObject> vos = new ArrayList<>();
         for (Question question : questions) {
             ViewObject vo = new ViewObject();
             User user = userService.getUser(question.getUserId());
             if (question.getContent().length()>200)
                question.setContent(question.getContent().substring(200));
             vo.set("question",question);
             vo.set("user",user);
             vo.set("followCount",followService.getFollowerCount(EntityType.TYPE_USER,userId));
             if (hostHolder.getUser() != null)
                if (followService.isFollower(hostHolder.getUser().getId(),EntityType.TYPE_QUESTION,question.getId()))
                     vo.set("followed",true);
             vo.set("followerCount",followService.getFollowerCount(EntityType.TYPE_USER,user.getId()));
             vos.add(vo);
         }
         return vos;
     }
}
