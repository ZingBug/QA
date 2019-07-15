package com.zingbug.qa.async;

import java.util.List;

/**
 * Created by ZingBug on 2019/7/2.
 */
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
