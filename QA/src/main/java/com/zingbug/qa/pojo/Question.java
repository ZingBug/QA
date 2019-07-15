package com.zingbug.qa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Getter
@Setter
@ToString
public class Question {

    private Integer id;
    private String title;
    private Integer userId;
    private String content;
    private Date createDate;
    private Integer commentCount;
}
