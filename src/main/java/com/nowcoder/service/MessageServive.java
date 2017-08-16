package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by albert on 2017/8/12.
 */
@Service
public class MessageServive {

    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> selectMessageByConversationId(String conversationId, int offset, int limit){
        return messageDAO.selectMessageByConversationId(conversationId,offset,limit);
    }

    public List<Message> selectMessageListByUserId(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getConversationCountUnread(int userId, String conversationId){
        return messageDAO.getConversationCountUnread(userId,conversationId);
    }

    public int updateHasRead(int id){
        return messageDAO.updateHasRead(id);
    }
}
