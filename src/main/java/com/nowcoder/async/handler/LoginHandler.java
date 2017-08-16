package com.nowcoder.async.handler;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventHandler;
import com.nowcoder.model.Message;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageServive;
import com.nowcoder.service.UserService;
import com.nowcoder.util.MailSender;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.*;

/**
 * Created by albert on 2017/8/15.
 */
@Component
public class LoginHandler implements EventHandler {
    private static final String SUBJECT = "欢迎加入家有大厨大家庭，亲记得激活邮箱";
    @Autowired
    MessageServive messageServive;
    @Autowired
    UserService userService;
    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EvenModel event) {
        Map<String,Object> model = new HashedMap();
        model.put("activeUrl",event.getExt("activeUrl"));
        model.put("userId",event.getActorId());
        mailSender.sendWithHTMLTemplate(event.getExt("emailDress"),SUBJECT,"regemail.html",model);
    }

    @Override
    public List<EvenType> getSupportEventTypes() {
        return Arrays.asList(EvenType.LOGIN);
    }
}
