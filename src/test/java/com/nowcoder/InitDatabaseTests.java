package com.nowcoder;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.MessageDAO;
import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.CommentService;
import com.nowcoder.util.WendaUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class InitDatabaseTests {

    @Autowired
    CommentService commentService;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 1; i < 200; i++) {
            commentService.updateTest(random.nextInt(112),random.nextInt(2)+1,i);
//            Message message = new Message();
//            message.setFromId(random.nextInt(101));
//            message.setToId(random.nextInt(101));
//            message.setContent(String.format("d%dadf%d",i,random.nextInt()));
//            message.setConversationId(i);
//            Date date = new Date();
//            date.setTime(date.getTime()-1000*i*60*random.nextInt(100000));
//            message.setCreatedDate(date);
//            messageDAO.addMessage(message);
//
//            Comment comment = new Comment();
//            comment.setCreatedDate(date);
//            comment.setContent(String.format("d%dadf%d",i,random.nextInt()));
//            comment.setUserId(random.nextInt(101));
//            comment.setEntityId(random.nextInt());
//            comment.setEntityType(String.format("%d",random.nextInt()));
//            commentDAO.addComment(comment);
//
//            User user = new User();
//            user.setName("fasd" + i);
//            user.setSalt(UUID.randomUUID().toString().substring(0,5));
//            user.setPassword(WendaUtil.MD5(i+user.getSalt()));
//            user.setHeadUrl(String.format("http://img.jsqq.net/uploads/allimg/150111/1_150111080328_%d.jpg",new Random().nextInt(100)));
//            userDAO.addUser(user);

//            Question question = new Question();
//            question.setCreatedDate(date);
//            question.setCommentCount(random.nextInt(100));
//            question.setContent(UUID.randomUUID().toString());
//            question.setTitle(UUID.randomUUID().toString());
//            question.setUserId(random.nextInt(106));
//            questionDAO.addQuestion(question);
        }
    }
}
