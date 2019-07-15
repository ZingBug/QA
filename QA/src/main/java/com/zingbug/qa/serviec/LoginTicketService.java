package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.LoginTicket;

/**
 * Created by ZingBug on 2019/6/30.
 */
public interface LoginTicketService {

    String addLoginTicket(int userId);

    void updateStatus(String ticket,int status);

    LoginTicket selectByTicket(String ticket);
}
