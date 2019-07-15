package com.zingbug.qa.dao;

import com.zingbug.qa.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * Created by ZingBug on 2019/6/29.
 */
@Repository
public interface UserDao {

    int addUser(User user);

    User selectById(int id);

    int updateById(User user);

    int deleteById(int id);

    User selectByName(String name);
}
