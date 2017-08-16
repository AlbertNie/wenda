package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 2017/8/11.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageServive messageServive;
    @Autowired
    UserService userService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/msg/list",method = {RequestMethod.GET})
    public String getConversationList(Model model){

        User user = hostHolder.getUser();
        if (user == null) return "redirect:/reglogin";
        List<Message> conversationList = messageServive.selectMessageListByUserId(user.getId(),0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message conversation : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation",conversation);
            vo.set("user",userService.getUser(conversation.getFromId()));
            vo.set("unRead",messageServive.getConversationCountUnread(user.getId(),conversation.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(value = "/msg/detail")
    public String getConversationDetails(@RequestParam(value = "conversationId",required = false) String conversationId,
                                         Model model,
                                         HttpServletRequest request){
        User thisUser = hostHolder.getUser();
        if (thisUser == null) return "redirect:/reglogin";

        if (conversationId == null)
            conversationId = (String) request.getAttribute("conversationId");
        if (conversationId == null)
            return "redirect:/msg/list";
        if (!conversationId.matches("^\\d+_\\d+$"))
            return "redirect:/msg/list";
        String num[] = conversationId.split("_");
        boolean flage = false;
        int ids[] = new int[2];
        for (int i = 0; i < 2; i++) {
            ids[i] = Integer.valueOf(num[i]);
            if (ids[i] == thisUser.getId())
                flage = true;
        }
        if (!flage)
            return "redirect:/msg/list";
        int thatUser = ids[0]==thisUser.getId()?ids[1]:ids[0];

        List<Message> messagess = messageServive.selectMessageByConversationId(conversationId,0,6);
        List<ViewObject> messages = new ArrayList<>();
        for (Message me : messagess) {
            if (me.getToId() == thisUser.getId())
                messageServive.updateHasRead(me.getId());
            ViewObject vo = new ViewObject();
            vo.set("message",me);
            vo.set("user",userService.getUser(me.getFromId()));
            if (me.getFromId() == thisUser.getId())
                vo.set("mySelf",true);
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
        model.addAttribute("thatUser",thatUser);
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/addMessage",method = {RequestMethod.POST})
    @ResponseBody
    public String sendMessage(@RequestParam("content") String content,
                              @RequestParam("toName") String toName){
        User user = hostHolder.getUser();
        if (user == null)
            return WendaUtil.toJsonCode(999,"未登陆");
        User toUser = userService.getUserByName(toName);
        if (toName == null)
            return WendaUtil.toJsonCode(1,"用户不存在");
        try {
            content = sensitiveService.filte(content);
            content = HtmlUtils.htmlEscape(content);
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setToId(toUser.getId());
            message.setFromId(user.getId());
            message.setHasRead(0);
            message.setConversationId(String.format("%d_%d",user.getId(),toUser.getId()));

            messageServive.addMessage(message);
        } catch (Exception e) {
            logger.error(user.getId() + "发送消息失败：消息内容：" + content+ " 错误消息："+ e.getMessage());
            return WendaUtil.toJsonCode(1,"发送失败");
        }
        return WendaUtil.toJsonCode(0);
    }

    @RequestMapping(value = "/msg/sendMessage",method = {RequestMethod.POST})
    public String sendMessageInDetail(@RequestParam("content") String content,
                                      @RequestParam("toId") int toId,
                                      HttpServletRequest request){
        User user = hostHolder.getUser();
        if (user == null)
            return "redirect:/reglogin";
        Message message = new Message();
        try {
            content = sensitiveService.filte(content);
            content = HtmlUtils.htmlEscape(content);

            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setToId(toId);
            message.setFromId(user.getId());
            message.setHasRead(0);
            message.setConversationId(String.format("%d_%d",user.getId(),toId));

            messageServive.addMessage(message);
            request.setAttribute("conversationId",message.getConversationId());
        } catch (Exception e) {
            logger.error(user.getId() + "发送消息失败：消息内容：" + content+ " 错误消息："+ e.getMessage());
        }
        return "forward:/msg/detail";
    }




}
