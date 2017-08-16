package com.nowcoder.controller;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventProducer;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by albert on 2017/8/10.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String activeUrl = "http://127.0.0.1:8080/active/";

    @Autowired
    UserService userService;
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    EventProducer eventProducer;


    @RequestMapping(value = {"/reg/"},method = {RequestMethod.POST})
    public String regist(Model model,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam(value = "next") String next,
                         HttpServletResponse response){
        try {
            Map<String,String> map = userService.register(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);

                //注册之后给一个事件去后台发邮件，要求激活
                User user = userService.getUserByName(username);
                EvenModel evenModel = new EvenModel(EvenType.LOGIN);
                evenModel.setActorId(user.getId());
                String uuId = UUID.randomUUID().toString().replaceAll("-","").substring(15);
                evenModel.setExt("activeUrl",activeUrl+uuId);
                evenModel.setExt("emailDress",username);
                eventProducer.fireEvent(evenModel);

                //在redis中加入未激活的名单
                jedisUtil.sAdd(RedisKeyUtil.getUnactiveKey(),String.valueOf(user.getId()));
                jedisUtil.sAdd(RedisKeyUtil.getUnactiveKey(),uuId);


                if (!StringUtils.isEmpty(next)){
//                    if (next.contains("."))
//                        next = "/";
                    return "redirect:" + next;
                }else
                    return "redirect:/";
            }else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常: " + e.getMessage());
            return "login";
        }
    }

    //激活控制
    @RequestMapping(value = {"/active/{uuId}/{userId}"},method = {RequestMethod.GET})
    public String active(@PathVariable("userId") int userId,
                         @PathVariable("uuId") String uuId){
        jedisUtil.srem(RedisKeyUtil.getUnactiveKey(),String.valueOf(userId));
        jedisUtil.srem(RedisKeyUtil.getUnactiveKey(),uuId);
        return "redirect:/";
    }

    @RequestMapping(value = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next") String next,
                        @RequestParam(value = "rememberme", defaultValue="false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String,String> map = userService.login(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if (!StringUtils.isEmpty(next)){
//                    if (next.contains("."))
//                        next = "";
                    return "redirect:" + next;
                }else
                    return "redirect:/";
            }else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登陆异常: " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(value = {"/reglogin"},method = {RequestMethod.GET})
    public String regist(Model model,
                         @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(value = {"/logout"})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(value = {"/unactive/{userId}"})
    public String unactive(@PathVariable("userId") int userId,
                           Model model){
        User user = userService.getUser(userId);
        model.addAttribute("userName",user.getName());
        return "unactive";
    }
}
