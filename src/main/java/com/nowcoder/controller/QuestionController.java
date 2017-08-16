package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 2017/8/11.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestions(@RequestParam("title") String title,
                               @RequestParam("content") String content){
        if (hostHolder.getUser() == null)
            return WendaUtil.toJsonCode(999);
       //html关键词转意
        try {
            title = HtmlUtils.htmlEscape(title);
            content = HtmlUtils.htmlEscape(content);
            //敏感词过滤
            title = sensitiveService.filte(title);
            content = sensitiveService.filte(content);

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCommentCount(0);
            question.setUserId(hostHolder.getUser().getId());
            question.setCreatedDate(new Date());
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.toJsonCode(0);
            }
        } catch (Exception e) {
            logger.error("用户："+hostHolder.getUser().getId()+"添加问题失败.");
        }
        return WendaUtil.toJsonCode(1,"添加失败");
    }

    @RequestMapping("/question/{questionId}")
    public String questionDetail(Model model,
                                 @PathVariable("questionId") int id){
        Question question = questionService.selectQuestionById(id);
        User user = userService.getUser(question.getUserId());
        model.addAttribute("user",user);
        model.addAttribute("question",question);
        List<Comment> commentsliset = commentService.getLatestComment(id, EntityType.TYPE_QUESTION,0,10);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentsliset) {
            ViewObject vo = new ViewObject();
            System.out.println(comment.getContent());
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            vo.set("likeCount",likeService.likeCount(EntityType.TYPE_COMMENT,comment.getId()));
            if (hostHolder.getUser() == null)
                vo.set("liked",0);
            else
                vo.set("liked",likeService.getStatus(hostHolder.getUser().getId(),EntityType.TYPE_COMMENT,comment.getId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }
}
