package com.nowcoder.async;

import java.util.List;

/**
 * Created by albert on 2017/8/15.
 */
public interface EventHandler {
    void doHandle(EvenModel event);

    List<EvenType> getSupportEventTypes();
}
