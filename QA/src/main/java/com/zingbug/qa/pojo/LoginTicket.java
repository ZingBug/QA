package com.zingbug.qa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Getter
@Setter
@ToString
public class LoginTicket {

    private Integer id;
    private Integer userId;
    private String ticket;
    private Date expired;
    private int status;
}
