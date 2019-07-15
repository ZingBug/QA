package com.zingbug.qa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Setter
@Getter
@ToString
public class Comment {
    private Integer id;
    private Integer userId;
    private String content;
    private Date createDate;
    private Integer entityId;
    private Integer entityType;
    private int status;
}
