package com.zingbug.qa.model;

import com.zingbug.qa.pojo.Question;
import com.zingbug.qa.pojo.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Getter
@Setter
public class ViewObject {

    private Map<String,Object> objs=new HashMap<>();

    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key)
    {
        return objs.get(key);
    }

}
