package com.nowcoder.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by albert on 2017/8/9.
 */
public class ViewObject {
    private Map<String,Object> objs = new HashedMap();

    public void set(String name, Object object){
        objs.put(name,object);
    }

    public Object get(String key){
        return objs.get(key);
    }

}
