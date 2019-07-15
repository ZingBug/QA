package com.zingbug.qa.serviec;

import com.zingbug.qa.pojo.User;

import java.util.Map;

/**
 * Created by ZingBug on 2019/6/29.
 */
public interface UserService {

    int addUser(User user);

    User selectById(int id);

    int updateById(User user);

    int deleteById(int id);

    User selectByName(String name);

    Map<String,String> register(String username,String password);

    Map<String,String> login(String username,String password);

    void logout(String ticket);
}
