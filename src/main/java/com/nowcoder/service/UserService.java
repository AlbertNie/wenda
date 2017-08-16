package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by albert on 2017/8/9.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

    public Map<String,String> register(String userName, String password){
        Map<String,String> map = new HashedMap();
        if (StringUtils.isEmpty(userName)){
            map.put("msg","名字不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        if (!userName.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
                && !userName.matches("^1[3|4|5|7|8]\\d{9}$")){
            map.put("msg","请使用邮箱或者手机号");
            return map;
        }
        if (userDAO.selectByName(userName) != null){
            map.put("msg","用户名已存在");
            return map;
        }

        User user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        user.setHeadUrl(String.format("http://img.jsqq.net/uploads/allimg/150111/1_150111080328_%d.jpg",new Random().nextInt(100)));
        userDAO.addUser(user);

        String ticket = addTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public Map<String,String> login(String userName, String password) {
        Map<String,String> map = new HashedMap();
        if (StringUtils.isEmpty(userName)){
            map.put("msg","名字不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(userName);
        if (user == null){
            map.put("msg","用户不存在");
            return map;
        }
        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public void logout(String ticket){
        loginTicketDAO.updateTicketByTicket(1,ticket);
    }

    private String addTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        Date now  = new Date();
        now.setTime(now.getTime()+3600*24*7*1000);
        loginTicket.setExpired(now);
        loginTicketDAO.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }
}
