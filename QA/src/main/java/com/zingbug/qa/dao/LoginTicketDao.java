package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.LoginTicket;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Repository
public interface LoginTicketDao {

    int addLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

    int deleteByTicket(String ticket);

}
