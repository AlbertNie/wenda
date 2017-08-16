package com.nowcoder.async;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by albert on 2017/8/15.
 */
public class EvenModel {
    //事件的类型
    private EvenType type;
    //事件的触发者ID
    private int actorId;
    //事件触发的载体
    private int entityType;
    private int entityId;
    private int entityOwnerId;

    //扩展的字段，为了存储更多的消息
    private Map<String,String> exts = new HashedMap();

    public EvenModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public EvenModel(){

    }

    public EvenModel(EvenType evenType){
        this.type = evenType;
    }

     public String getExt(String key){
        return exts.get(key);
     }

    public EvenType getType() {
        return type;
    }

    public EvenModel setType(EvenType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EvenModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EvenModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EvenModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EvenModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EvenModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
