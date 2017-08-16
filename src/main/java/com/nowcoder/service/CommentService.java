package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by albert on 2017/8/11.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public void deleteComment(int id){
        commentDAO.updateComment(id);
    }

    public List<Comment> getLatestComment(int entityId,int entityType,int offset, int limit){
        return commentDAO.selectCommentByEntity(entityId,entityType,offset,limit);
    }

    public void updateTest(int entityId,int entityType,int id){

        commentDAO.updateCommentTest(entityId,entityType,id);
    }

    public Comment getCommentById(int id){
        return commentDAO.getCommentById(id);
    }
}
