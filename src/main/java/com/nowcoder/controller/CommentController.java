package com.nowcoder.controller;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventProducer;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SensitiveService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * Created by albert on 2017/8/11.
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping("/addComment")
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        if (hostHolder.getUser() == null)
            return "redirect:/reglogin";
        content = HtmlUtils.htmlEscape(content);
        content = sensitiveService.filte(content);

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setStatus(0);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.TYPE_QUESTION);
            comment.setUserId(hostHolder.getUser().getId());
           //更新评论总数
            Question question = questionService.selectQuestionById(questionId);
            question.setCommentCount(question.getCommentCount()+1);
            commentService.addComment(comment);
            questionService.updateQuestionCommentCount(question);
            //发起评论问题的事件
            EvenModel evenModel = new EvenModel(EvenType.COMMENT);
            evenModel.setActorId(hostHolder.getUser().getId());
            evenModel.setEntityType(EntityType.TYPE_COMMENT);
            evenModel.setEntityId(comment.getId());
            eventProducer.fireEvent(evenModel);

        } catch (Exception e) {
            logger.error("用户:"+hostHolder.getUser().getId()+"添加问题评论失败"+e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }

}
