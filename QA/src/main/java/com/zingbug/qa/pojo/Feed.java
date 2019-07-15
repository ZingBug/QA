package com.zingbug.qa.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by ZingBug on 2019/7/3.
 */
@Getter
@Setter
public class Feed {

    private Integer id;
    private Integer type;
    private Integer userId;
    private Date createDate;
    //JSON格式
    private String data;

    private JSONObject dataJson;

    public void setData(String data)
    {
        this.data=data;
        dataJson=JSONObject.parseObject(data);
    }

    public String get(String key)
    {
        return dataJson==null?null:dataJson.getString(key);
    }
}
