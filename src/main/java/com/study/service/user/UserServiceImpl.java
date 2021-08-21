package com.study.service.user;

import com.study.dao.BaseDao;
import com.study.dao.user.UserDao;
import com.study.dao.user.UserDaoImpl;
import com.study.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    // 业务层调用Dao层，引入dao
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection connection = BaseDao.getConnection();
        User user = null;

        try {
            // 通过业务层调用对应的具体的数据库操作
            user = userDao.getLoginUser(connection, userCode);
            if (user != null && !user.getUserPassword().equals(password))
                user = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return user;
    }

    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login("admin", "123456sadf7");
        System.out.println("PASSWORD:" + user.getUserPassword());
    }
}
