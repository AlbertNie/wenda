package com.nowcoder.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by albert on 2017/8/18.
 */
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    private JSONObject jsonObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public String get(String key){
        return jsonObject==null?null:jsonObject.getString(key);
    }

    public void setData(String data) {
        jsonObject = JSONObject.parseObject(data);
        this.data = data;
    }
}
