package com.nowcoder.controller;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventProducer;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 2017/8/11.
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;

    @RequestMapping("/search")
    public String addComment(@RequestParam("q") String keyWord,
                             @RequestParam(value = "offset",defaultValue = "0") int offset,
                             @RequestParam(value = "count",defaultValue = "10") int count,
                             Model model){
        try {
            List<Question> questions = searchService.searchQuestion(keyWord,offset,count,"<strong>","</strong>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question q : questions) {
                ViewObject vo = new ViewObject();
                Question question = questionService.selectQuestionById(q.getId());
                if (q.getContent() != null)
                    question.setContent(q.getContent());
                if (q.getTitle() != null)
                    question.setTitle(q.getTitle());
                User user = userService.getUser(question.getUserId());
                if (question.getContent().length()>300) {
                    String content = question.getContent();
                    content = HtmlUtils.htmlEscape(content);
                    content = content.substring(0,300);
                    question.setContent(content);
                }


                vo.set("question",question);
                vo.set("user",user);
                vo.set("followCount",followService.getFollowerCount(EntityType.TYPE_USER,user.getId()));
                if (hostHolder.getUser() != null)
                    if (followService.isFollower(hostHolder.getUser().getId(),EntityType.TYPE_QUESTION,question.getId()))
                        vo.set("followed",true);
                vo.set("followerCount",followService.getFollowerCount(EntityType.TYPE_USER,user.getId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("keyWord",keyWord);
        } catch (Exception e) {
            logger.error("搜索失败："+e.getMessage());
        }
        return "result";
    }

}
