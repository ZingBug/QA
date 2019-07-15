package com.zingbug.qa.model;

import com.zingbug.qa.pojo.User;
import org.springframework.stereotype.Component;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser()
    {
        return users.get();
    }

    public void setUser(User user)
    {
        users.set(user);
    }

    public void clear()
    {
        users.remove();
    }
}
