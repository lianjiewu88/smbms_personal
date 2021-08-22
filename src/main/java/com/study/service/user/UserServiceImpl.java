package com.study.service.user;

import com.study.dao.BaseDao;
import com.study.dao.user.UserDao;
import com.study.dao.user.UserDaoImpl;
import com.study.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    // 业务层调用Dao层，引入dao
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    // 用户登录
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
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

    // 修改用户密码
    public boolean updatePwd(int userId, String password) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection, userId, password) == 1) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }


    // 查询用户数量
    public int getUserAmount(String userName, int userRole) {
        Connection connection = null;
        int userAmount = 0;
        try {
            connection = BaseDao.getConnection();
            userAmount = userDao.getUserAmount(connection, userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return userAmount;
    }


    public List<User> getUserListByConditions(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return userList;
    }


}
