package com.zingbug.qa.async;

import com.alibaba.fastjson.JSONObject;
import com.zingbug.qa.util.JedisAdapter;
import com.zingbug.qa.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZingBug on 2019/7/2.
 */
@Service
@Slf4j
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;

    //发送EventModel
    public boolean fireEvent(EventModel eventModel)
    {
        try {
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);//除了用redis外，也可以用阻塞式的优先级队列
            return true;
        }catch (Exception e)
        {
            log.error("EventProducer发送失败"+e.getMessage());
            return false;
        }
    }
}
