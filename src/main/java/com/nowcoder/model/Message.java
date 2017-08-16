package com.nowcoder.model;

import java.util.Date;

/**
 * Created by albert on 2017/8/9.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private String conversationId;
    private Date createdDate;
    private int hasRead;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        if (fromId<toId){
            return String.format("%d_%d",fromId,toId);
        }else return String.format("%d_%d",toId,fromId);
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
