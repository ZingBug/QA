package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.LoginTicketDao;
import com.zingbug.qa.pojo.LoginTicket;
import com.zingbug.qa.serviec.LoginTicketService;
import com.zingbug.qa.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Service
public class LoginTicketServiceImpl implements LoginTicketService {

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Override
    public String addLoginTicket(int userId) {
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setExpired(DateUtil.getAfterDate(new Date(),10, Calendar.DATE));
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    @Override
    public void updateStatus(String ticket, int status) {
        loginTicketDao.updateStatus(ticket,status);
    }

    @Override
    public LoginTicket selectByTicket(String ticket) {
        return loginTicketDao.selectByTicket(ticket);
    }
}
