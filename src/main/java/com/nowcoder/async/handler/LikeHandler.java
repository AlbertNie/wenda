package com.nowcoder.async.handler;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventHandler;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageServive;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 2017/8/15.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageServive messageServive;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EvenModel event) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_ID);
        message.setToId(event.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setContent("用户 " + userService.getUser(event.getActorId()).getName() +
                "在刚才给你点了赞，http://127.0.0.1:8080/question/"+event.getExt("questionId"));
        messageServive.addMessage(message);
    }

    @Override
    public List<EvenType> getSupportEventTypes() {
        return Arrays.asList(EvenType.LIKE);
    }
}
