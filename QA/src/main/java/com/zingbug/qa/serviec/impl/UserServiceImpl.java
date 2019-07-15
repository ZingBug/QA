package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.dao.UserDao;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.LoginTicketService;
import com.zingbug.qa.serviec.UserService;
import com.zingbug.qa.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.cs.US_ASCII;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketService loginTicketService;

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User selectById(int id) {
        return userDao.selectById(id);
    }

    @Override
    public User selectByName(String name) {
        return userDao.selectByName(name);
    }

    @Override
    public int updateById(User user) {
        return userDao.updateById(user);
    }

    @Override
    public int deleteById(int id) {
        return userDao.deleteById(id);
    }

    @Override
    public Map<String, String> register(String username, String password) {
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isBlank(username))
        {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password))
        {
            map.put("msg","密码不能为空");
            return map;
        }

        User user=userDao.selectByName(username);
        if(user!=null)
        {
            map.put("msg","用户名已被注册");
            return map;
        }

        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("https://images.nowcoder.com/head/%dm.png",new Random().nextInt(100)));
        user.setPassword(MD5Util.MD5(username+user.getSalt()));
        userDao.addUser(user);

        //登陆
        String ticket=loginTicketService.addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    @Override
    public Map<String, String> login(String username, String password) {
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isBlank(username))
        {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password))
        {
            map.put("msg","密码不能为空");
            return map;
        }

        User user=userDao.selectByName(username);
        if(user==null)
        {
            map.put("msg","用户名不存在");
            return map;
        }

        if(!MD5Util.MD5(password+user.getSalt()).equals(user.getPassword()))
        {
            map.put("msg","密码不正确");
            return map;
        }

        String ticket=loginTicketService.addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketService.updateStatus(ticket,1);
    }
}
