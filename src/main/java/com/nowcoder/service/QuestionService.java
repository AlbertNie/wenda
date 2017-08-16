package com.nowcoder.service;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

    public int addQuestion(Question question){
        return questionDAO.addQuestion(question);
    }

    public Question selectQuestionById(int id){
        return questionDAO.selectQuestionById(id);
    }

    public void updateQuestionCommentCount(Question question){
        questionDAO.updateQuestionCommentCount(question);
    }
}
