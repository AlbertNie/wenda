package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by albert on 2017/8/10.
 */
@Component
public class HostHolder {
    private static final ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void set(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
