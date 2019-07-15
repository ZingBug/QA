package com.zingbug.qa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Getter
@Setter
@ToString
public class User {

    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
}
